package service;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import model.Board;
import model.Cell;
import org.junit.jupiter.api.Test;
import service.state.BoardStateService;

class BoardStateServiceTest {

  @Test
  void testCalculateNextStateTriangle() {
    Board board = givenTriangleBoard();

    Board result = new BoardStateService(board).calculateNextState();

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

    Board result = new BoardStateService(board).calculateNextState();

    List<Cell> aliveCells = retrieveAliveCells(result);
    List<Cell> expectedAlive = range(0, 3).mapToObj(col -> new Cell(1, col)).collect(toList());
    assertEquals(expectedAlive, aliveCells);
  }

  @Test
  void testCalculateNextStateMiddleBoard() {
    Board board = givenMiddleBoard();

    Board result = new BoardStateService(board).calculateNextState();

    assertEquals(List.of(new Cell(1, 1)), retrieveAliveCells(result));
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
    Board board = new Board(3, 3);
    board.born(new Cell(0, 1));
    board.born(new Cell(0, 2));
    board.born(new Cell(1, 2));

    return board;
  }

  // · ■ ·
  // · ■ ·
  // · ■ ·
  private Board givenToggleBoard() {
    Board board = new Board(3, 3);
    board.born(new Cell(0, 1));
    board.born(new Cell(1, 1));
    board.born(new Cell(2, 1));

    return board;
  }

  // ■ · ·
  // · · ■
  // ■ · ·
  private Board givenMiddleBoard() {
    Board board = new Board(3, 3);
    board.born(new Cell(0, 0));
    board.born(new Cell(1, 2));
    board.born(new Cell(2, 0));

    return board;
  }
}
