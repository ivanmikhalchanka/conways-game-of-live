package service.game;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import common.ThreadUtils;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import model.CachingBoard;
import renderer.BoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.state.BoardPartStateService;

public class CyclicBarrierGameService implements GameService {
  private final CachingBoard board;
  private final CyclicBarrier barrier;
  private final BoardRenderer renderer;
  private final List<Worker> workers;

  public CyclicBarrierGameService(
      CachingBoard board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    this.board = board;
    this.renderer = renderer;

    int availableThreads = ThreadUtils.getNumberOfAvailableThreads();
    barrier = new CyclicBarrier(availableThreads, this::processIterationComplete);

    workers =
        range(0, availableThreads)
            .mapToObj(index -> this.board.getPartOfCells(availableThreads, index))
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

    workers.stream()
        .map(Thread::new)
        .forEach(Thread::start);
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
