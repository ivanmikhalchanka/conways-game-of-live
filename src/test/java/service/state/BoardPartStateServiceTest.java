package service.state;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import model.Board;
import model.Cell;
import model.SimpleBoard;
import org.junit.jupiter.api.Test;

class BoardPartStateServiceTest {
  @Test
  void testCalculateNextStateTriangle() {
    Board board = givenTriangleBoard();
    BoardPartStateService stateService = givenStateServiceForWholeBoard(board);

    Board result = stateService.moveToNextState(board);

    List<Cell> aliveCells = retrieveAliveCells(result);
    List<Cell> expectedAlive =
        List.of(
            new Cell(0, 1), new Cell(0, 2),
            new Cell(1, 1), new Cell(1, 2));
    assertEquals(expectedAlive, aliveCells);
  }

  @Test
  void testCalculateNextStateToggle() {
    Board board = givenToggleBoard();
    BoardPartStateService stateService = givenStateServiceForWholeBoard(board);

    Board result = stateService.moveToNextState(board);

    List<Cell> aliveCells = retrieveAliveCells(result);
    List<Cell> expectedAlive = range(0, 3).mapToObj(col -> new Cell(1, col)).collect(toList());
    assertEquals(expectedAlive, aliveCells);
  }

  @Test
  void testCalculateNextStateMiddleBoard() {
    Board board = givenMiddleBoard();
    BoardPartStateService stateService = givenStateServiceForWholeBoard(board);

    Board result = stateService.moveToNextState(board);

    assertEquals(List.of(new Cell(1, 1)), retrieveAliveCells(result));
  }

  BoardPartStateService givenStateServiceForWholeBoard(Board board) {
    List<Cell> cells = Cell.buildCells(board.getNumOfRows(), board.getNumOfColumns())
        .collect(toList());

    return new BoardPartStateService(cells);
  }

  List<Cell> retrieveAliveCells(Board board) {
    return Cell.buildCells(board.getNumOfRows(), board.getNumOfColumns())
        .filter(board::isAlive)
        .collect(toList());
  }

  // · ■ ■
  // · · ■
  // · · ·
  private Board givenTriangleBoard() {
    Board board = new SimpleBoard(3, 3);
    board.born(new Cell(0, 1));
    board.born(new Cell(0, 2));
    board.born(new Cell(1, 2));

    return board;
  }

  // · ■ ·
  // · ■ ·
  // · ■ ·
  private Board givenToggleBoard() {
    Board board = new SimpleBoard(3, 3);
    board.born(new Cell(0, 1));
    board.born(new Cell(1, 1));
    board.born(new Cell(2, 1));

    return board;
  }

  // ■ · ·
  // · · ■
  // ■ · ·
  private Board givenMiddleBoard() {
    Board board = new SimpleBoard(3, 3);
    board.born(new Cell(0, 0));
    board.born(new Cell(1, 2));
    board.born(new Cell(2, 0));

    return board;
  }
}
