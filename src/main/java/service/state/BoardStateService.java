package service.state;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

import common.ComputationDelayEmulator;
import java.util.List;
import java.util.Map;
import model.Board;
import model.Cell;

public class BoardStateService {

  public static final int BORN_THRESHOLD = 3;
  public static final int STAY_ALIVE = 2;
  private final Board board;

  public BoardStateService(Board board) {
    this.board = board;
  }

  public Board calculateNextState() {
    List<Cell> cells =
        Cell.buildCells(board.getNumOfRows(), board.getNumOfColumns()).collect(toList());

    Board result = new Board(this.board.getNumOfRows(), this.board.getNumOfColumns());

    return processCells(cells, result);
  }

  Board processCells(List<Cell> cells, Board result) {
    Map<Boolean, List<Cell>> cellsByAliveFlag =
        cells.stream().collect(partitioningBy(this::shouldBeAlive));

    cellsByAliveFlag.get(true).forEach(result::born);
    cellsByAliveFlag.get(false).forEach(result::kill);

    ComputationDelayEmulator.emulateComputationDelay(cells.size());

    return result;
  }

  boolean shouldBeAlive(Cell cell) {
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
