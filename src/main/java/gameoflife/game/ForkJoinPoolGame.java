package gameoflife.game;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.ActivatedCell;
import gameoflife.game.state.BoardPartStateService;
import gameoflife.game.state.CachingBoard;
import gameoflife.model.Board;
import gameoflife.model.Cell;
import gameoflife.renderer.BoardRenderer;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ForkJoinPoolGame implements Game {
  private final CachingBoard board;
  private final BoardRenderer renderer;
  private final ComputationDelayEmulator delayEmulator;
  private final ForkJoinPool forkJoinPool;

  public ForkJoinPoolGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    this.board = new CachingBoard(board);
    this.renderer = renderer;
    this.delayEmulator = delayEmulator;

    forkJoinPool = ForkJoinPool.commonPool();
  }

  @Override
  public void start() {
    int boardSize = board.getNumOfColumns() * board.getNumOfRows();
    int threshold = boardSize / forkJoinPool.getParallelism();

    while (!board.hasConverged()) {
      renderer.render(board);
      board.commitChanges();

      ForkJoinBoardStateCalculator boardStateCalculator =
          new ForkJoinBoardStateCalculator(board.getAllCells(), threshold);
      Stream<ActivatedCell> changes = forkJoinPool.invoke(boardStateCalculator);

      BoardPartStateService.applyChanges(changes, board);
    }
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
