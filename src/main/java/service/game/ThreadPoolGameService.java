package service.game;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import service.emulator.ComputationDelayEmulator;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.CachingBoard;
import renderer.BoardRenderer;
import service.state.BoardPartStateService;

public class ThreadPoolGameService implements GameService {
  private final CachingBoard board;
  private final CyclicBarrier barrier;
  private final BoardRenderer renderer;
  private final List<Worker> workers;

  public ThreadPoolGameService(
      CachingBoard board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    this.board = board;
    this.renderer = renderer;

    int availableProcessors = Runtime.getRuntime().availableProcessors();
    barrier = new CyclicBarrier(availableProcessors, this::processIterationComplete);

    workers =
        range(0, availableProcessors)
            .mapToObj(index -> this.board.getPartOfCells(availableProcessors, index))
            .map(cells -> new BoardPartStateService(cells, delayEmulator))
            .map(Worker::new)
            .collect(toList());
  }

  void processIterationComplete() {
    board.commitChanges();
    renderer.render(board);
  }

  @Override
  public void start() {
    renderer.render(board);

    ExecutorService pool = Executors.newFixedThreadPool(workers.size());
    workers.forEach(pool::submit);

    pool.shutdown();
  }

  class Worker implements Runnable {
    private final BoardPartStateService boardPartStateService;

    Worker(BoardPartStateService boardPartStateService) {
      this.boardPartStateService = boardPartStateService;
    }

    @Override
    public void run() {
      while (!board.hasConverged()) {
        boardPartStateService.moveToNextState(board);

        try {
          barrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
          e.printStackTrace();
          Thread.currentThread().interrupt();
        }
      }
    }
  }
}
