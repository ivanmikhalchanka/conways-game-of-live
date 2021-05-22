package gameoflife.game;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.ActivatedCell;
import gameoflife.game.state.BoardPartStateService;
import gameoflife.model.Board;
import gameoflife.model.Cell;
import gameoflife.renderer.BoardRenderer;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ForkJoinPoolGame extends CachingBoardGame {
  private final ForkJoinPool forkJoinPool;

  public ForkJoinPoolGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    super(board, renderer, delayEmulator);

    forkJoinPool = ForkJoinPool.commonPool();
  }

  @Override
  public void start() {
    int boardSize = board.getNumOfColumns() * board.getNumOfRows();
    int threshold = boardSize / forkJoinPool.getParallelism();

    super.applyChangesUtilConverged(() -> calculateChanges(threshold));
  }

  Stream<ActivatedCell> calculateChanges(int threshold) {
    ForkJoinBoardStateCalculator boardStateCalculator =
        new ForkJoinBoardStateCalculator(board.getAllCells(), threshold);

    return forkJoinPool.invoke(boardStateCalculator);
  }

  /* With accordance to JMH results -
   * passing threshold to the left and rest to the right is more effective
   * than splitting into two batches with equal size
   */
  class ForkJoinBoardStateCalculator extends RecursiveTask<Stream<ActivatedCell>> {
    private final List<Cell> cellsToProcess;
    private final int threshold;

    ForkJoinBoardStateCalculator(List<Cell> cellsToProcess, int threshold) {
      this.cellsToProcess = cellsToProcess;
      this.threshold = threshold;
    }

    @Override
    protected Stream<ActivatedCell> compute() {
      if (cellsToProcess.size() <= threshold) {
        return new BoardPartStateService(cellsToProcess, delayEmulator).calculateNextState(board);
      }
      ForkJoinBoardStateCalculator leftTask = buildLeftTask();
      ForkJoinBoardStateCalculator rightTask = buildRightTask();

      return awaitForResults(leftTask, rightTask);
    }

    Stream<ActivatedCell> awaitForResults(
        ForkJoinBoardStateCalculator leftTask, ForkJoinBoardStateCalculator rightTask) {
      leftTask.fork();
      Stream<ActivatedCell> rightResult = rightTask.compute();
      Stream<ActivatedCell> leftResult = leftTask.join();

      return Stream.concat(leftResult, rightResult);
    }

    ForkJoinBoardStateCalculator buildLeftTask() {
      List<Cell> leftBatch = cellsToProcess.stream().limit(threshold).collect(Collectors.toList());

      return new ForkJoinBoardStateCalculator(leftBatch, threshold);
    }

    ForkJoinBoardStateCalculator buildRightTask() {
      List<Cell> rightBatch = cellsToProcess.stream().skip(threshold).collect(Collectors.toList());

      return new ForkJoinBoardStateCalculator(rightBatch, threshold);
    }
  }
}