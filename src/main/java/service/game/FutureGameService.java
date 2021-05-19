package service.game;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import model.ActivatedCell;
import model.CachingBoard;
import renderer.BoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.state.BoardPartStateService;

public class FutureGameService implements GameService {
  private final CachingBoard board;
  private final BoardRenderer renderer;
  private final List<BoardPartStateService> partStateServices;
  private final ExecutorService executorService;

  public FutureGameService(
      CachingBoard board,
      BoardRenderer renderer,
      ComputationDelayEmulator delayEmulator,
      ExecutorService executorService,
      int parallelism) {
    this.board = board;
    this.renderer = renderer;
    this.executorService = executorService;

    partStateServices =
        range(0, parallelism)
            .mapToObj(index -> this.board.getPartOfCells(parallelism, index))
            .map(cells -> new BoardPartStateService(cells, delayEmulator))
            .collect(toList());
  }

  @Override
  public void start() {
    ExecutorCompletionService<Stream<ActivatedCell>> completionService =
        new ExecutorCompletionService<>(executorService);

    while (!board.hasConverged()) {
      board.commitChanges();
      renderer.render(board);

      partStateServices.stream()
          .map(service -> (Callable<Stream<ActivatedCell>>) () -> service.calculateNextState(board))
          .forEach(completionService::submit);

      moveToNextState(completionService);
    }

    executorService.shutdown(); // IMPORTANT! Without this invocation ThreadPool will never finish.
  }

  private void moveToNextState(ExecutorCompletionService<Stream<ActivatedCell>> completionService) {
    IntStream.range(0, partStateServices.size())
        .mapToObj(i -> retrieveNext(completionService))
        .forEach(changes -> BoardPartStateService.applyChanges(changes, board));
  }

  Stream<ActivatedCell> retrieveNext(
      ExecutorCompletionService<Stream<ActivatedCell>> completionService) {
    try {
      Future<Stream<ActivatedCell>> future = completionService.take();

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
