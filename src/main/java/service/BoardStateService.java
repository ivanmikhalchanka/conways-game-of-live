package service;

import static java.util.stream.Collectors.partitioningBy;

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
    Map<Boolean, List<Cell>> aliveCells =
        Cell.buildCells(board.getNumOfRows(), board.getNumOfColumns())
            .collect(partitioningBy(this::shouldBeAlive));

    Board result = new Board(this.board.getNumOfRows(), this.board.getNumOfColumns());
    aliveCells.get(true).forEach(result::born);
    aliveCells.get(false).forEach(result::kill);

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
