package service.state;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import model.CachingBoard;
import model.Cell;

public class RunnableBoardPartStateService extends BoardPartStateService implements Runnable {
  private final CyclicBarrier barrier;
  private final CachingBoard board;

  public RunnableBoardPartStateService(CyclicBarrier barrier, CachingBoard board,
      List<Cell> cellsToProcess) {
    super(cellsToProcess);
    this.barrier = barrier;
    this.board = board;
  }

  @Override
  public void run() {
    while (!board.hasConverged()) {
      moveToNextState(board);

      try {
        barrier.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        e.printStackTrace();
      }
    }
  }
}
