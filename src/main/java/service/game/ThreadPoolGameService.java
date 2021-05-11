package service.game;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import model.Board;
import model.CachingBoard;
import renderer.BoardRenderer;
import service.state.BoardPartStateService;

public class ThreadPoolGameService implements GameService {
  private final CachingBoard board;
  private final CyclicBarrier barrier;
  private final BoardRenderer renderer;
  private final List<BoardPartStateService> partStateServices;

  public ThreadPoolGameService(Board board, BoardRenderer renderer) {
    this.board = new CachingBoard(board);
    this.renderer = renderer;

    int availableProcessors = Runtime.getRuntime().availableProcessors();
    barrier = new CyclicBarrier(availableProcessors, this::renderNewChanges);

    partStateServices =
        range(0, availableProcessors)
            .mapToObj(index -> this.board.getPartToProcess(availableProcessors, index))
            .map(cellsToProcess -> new BoardPartStateService(barrier, this.board, cellsToProcess))
            .collect(toList());
  }

  void renderNewChanges() {
    board.commitNewValues();
    renderer.render(board);
  }

  @Override
  public void start() {
    renderer.render(board);

    ExecutorService pool = Executors.newFixedThreadPool(partStateServices.size());
    partStateServices.forEach(pool::submit);

    try {
      pool.awaitTermination(1, TimeUnit.MINUTES);
      pool.shutdown();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
