package service.state;

import static java.util.stream.Collectors.partitioningBy;

import common.ComputationDelayEmulator;
import java.util.List;
import java.util.Map;
import model.Board;
import model.Cell;

public class BoardPartStateService {
  public static final int BORN_THRESHOLD = 3;
  public static final int STAY_ALIVE = 2;

  private final List<Cell> cellsToProcess;

  public BoardPartStateService(List<Cell> cellsToProcess) {
    this.cellsToProcess = cellsToProcess;
  }

  public Board moveToNextState(Board board) {
    Map<Boolean, List<Cell>> cellsByAliveFlag =
        cellsToProcess.stream().collect(partitioningBy(cell -> shouldBeAlive(cell, board)));

    cellsByAliveFlag.get(true).forEach(board::born);
    cellsByAliveFlag.get(false).forEach(board::kill);

    ComputationDelayEmulator.emulateComputationDelay(cellsToProcess.size());

    return board;
  }

  boolean shouldBeAlive(Cell cell, Board board) {
    long aliveNeighbours = board.getNeighbours(cell).stream().filter(board::isAlive).count();

    return board.isAlive(cell) ? shouldStayAlive(aliveNeighbours) : shouldBorn(aliveNeighbours);
  }

  boolean shouldStayAlive(long aliveNeighbours) {
    return aliveNeighbours >= STAY_ALIVE && aliveNeighbours <= BORN_THRESHOLD;
  }

  boolean shouldBorn(long aliveNeighbours) {
    return aliveNeighbours == BORN_THRESHOLD;
  }
}
