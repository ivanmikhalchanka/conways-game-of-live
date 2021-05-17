package service.state;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

import java.util.stream.Stream;
import model.ActivatedCell;
import service.emulator.ComputationDelayEmulator;
import java.util.List;
import java.util.Map;
import model.Board;
import model.Cell;

public class BoardPartStateService {
  public static final int BORN_THRESHOLD = 3;
  public static final int STAY_ALIVE = 2;

  private final List<Cell> cellsToProcess;
  private final ComputationDelayEmulator delayEmulator;

  public BoardPartStateService(List<Cell> cellsToProcess, ComputationDelayEmulator delayEmulator) {
    this.cellsToProcess = cellsToProcess;
    this.delayEmulator = delayEmulator;
  }

  public void moveToNextState(Board board) {
    Map<Boolean, List<Cell>> cellsByAliveFlag =
        calculateNextState(board)
            .collect(
                partitioningBy(ActivatedCell::isActive, mapping(ActivatedCell::getCell, toList())));

    cellsByAliveFlag.get(true).forEach(board::born);
    cellsByAliveFlag.get(false).forEach(board::kill);
  }

  public Stream<ActivatedCell> calculateNextState(Board board) {
    delayEmulator.emulateComputationDelay(cellsToProcess.size());

    return cellsToProcess.stream().map(cell -> new ActivatedCell(cell, shouldBeAlive(cell, board)));
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
