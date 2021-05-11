package service.state;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import model.CachingBoard;
import model.Cell;

public class BoardPartStateService extends BoardStateService implements Runnable {
  private final CyclicBarrier barrier;
  private final CachingBoard board;
  private final List<Cell> cellsToProcess;

  public BoardPartStateService(
      CyclicBarrier barrier, CachingBoard board, List<Cell> cellsToProcess) {
    super(board);
    this.barrier = barrier;
    this.board = board;
    this.cellsToProcess = cellsToProcess;
  }

  @Override
  public void run() {
    while (!board.hasConverged()) {
      processCells(cellsToProcess, board);

      try {
        barrier.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        e.printStackTrace();
      }
    }
  }
}
