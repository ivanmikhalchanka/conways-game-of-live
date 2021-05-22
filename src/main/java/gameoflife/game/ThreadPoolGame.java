package gameoflife.game;

import gameoflife.common.ThreadUtils;
import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.ActivatedCell;
import gameoflife.game.state.BoardPartStateService;
import gameoflife.model.Board;
import gameoflife.renderer.BoardRenderer;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ThreadPoolGame extends CachingBoardGame {
  private final List<BoardPartStateService> partStateServices;
  private final int availableThreads;

  public ThreadPoolGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    super(board, renderer, delayEmulator);
    availableThreads = ThreadUtils.getNumberOfAvailableThreads();
    partStateServices = super.buildBoardPartStateServices(availableThreads);
  }

  @Override
  public void start() {
    ExecutorService executorService = Executors.newFixedThreadPool(availableThreads);
    ExecutorCompletionService<Stream<ActivatedCell>> completionService =
        new ExecutorCompletionService<>(executorService);

    super.applyChangesUtilConverged(() -> calculateChanges(completionService));

    executorService.shutdown(); // IMPORTANT! Without this invocation ThreadPool will never finish.
  }

  Stream<ActivatedCell> calculateChanges(
      ExecutorCompletionService<Stream<ActivatedCell>> completionService) {
    partStateServices.stream()
        .map(service -> (Callable<Stream<ActivatedCell>>) () -> service.calculateNextState(board))
        .forEach(completionService::submit);

    return IntStream.range(0, partStateServices.size())
        .mapToObj(i -> retrieveNext(completionService))
        .flatMap(Function.identity());
  }

  Stream<ActivatedCell> retrieveNext(
      ExecutorCompletionService<Stream<ActivatedCell>> completionService) {
    try {
      Future<Stream<ActivatedCell>> future = completionService.take();

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
