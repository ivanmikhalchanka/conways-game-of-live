package service.game;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.Board;
import model.CachingBoard;
import renderer.BoardRenderer;
import service.state.RunnableBoardPartStateService;

public class ThreadPoolGameService implements GameService {
  private final CachingBoard board;
  private final CyclicBarrier barrier;
  private final BoardRenderer renderer;
  private final List<RunnableBoardPartStateService> partStateServices;

  public ThreadPoolGameService(Board board, BoardRenderer renderer) {
    this.board = new CachingBoard(board);
    this.renderer = renderer;

    int availableProcessors = Runtime.getRuntime().availableProcessors();
    barrier = new CyclicBarrier(availableProcessors, this::renderNewChanges);

    partStateServices =
        range(0, availableProcessors)
            .mapToObj(index -> this.board.getPartToProcess(availableProcessors, index))
            .map(cellsToProcess -> new RunnableBoardPartStateService(barrier, this.board, cellsToProcess))
            .collect(toList());
  }

  void renderNewChanges() {
    board.commitChanges();
    renderer.render(board);
  }

  @Override
  public void start() {
    renderer.render(board);

    ExecutorService pool = Executors.newFixedThreadPool(partStateServices.size());
    partStateServices.forEach(pool::submit);

    pool.shutdown();
  }
}
