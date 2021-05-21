package gameoflife.game;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.ActivatedCell;
import gameoflife.game.state.BoardPartStateService;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import gameoflife.model.Board;
import gameoflife.game.state.CachingBoard;
import gameoflife.renderer.BoardRenderer;

public class FutureGame implements Game {
  private final CachingBoard board;
  private final BoardRenderer renderer;
  private final List<BoardPartStateService> partStateServices;
  private final ExecutorService executorService;

  public FutureGame(
      Board board,
      BoardRenderer renderer,
      ComputationDelayEmulator delayEmulator,
      ExecutorService executorService,
      int parallelism) {
    this.board = new CachingBoard(board);
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

      Stream<ActivatedCell> results = waitForResults(completionService);
      BoardPartStateService.applyChanges(results, board);
    }

    executorService.shutdown(); // IMPORTANT! Without this invocation ThreadPool will never finish.
  }

  private Stream<ActivatedCell> waitForResults(
      ExecutorCompletionService<Stream<ActivatedCell>> completionService) {
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
