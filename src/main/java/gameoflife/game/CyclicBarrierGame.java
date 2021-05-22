package gameoflife.game;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import gameoflife.common.ThreadUtils;
import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.BoardPartStateService;
import gameoflife.model.Board;
import gameoflife.renderer.BoardRenderer;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierGame extends CachingBoardGame {
  private final CyclicBarrier barrier;
  private final List<Worker> workers;

  public CyclicBarrierGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    super(board, renderer, delayEmulator);

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

    workers.stream().map(Thread::new).forEach(Thread::start);
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
