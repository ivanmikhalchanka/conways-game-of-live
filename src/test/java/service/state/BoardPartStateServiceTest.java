package service.state;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.*;

import model.ActivatedCell;
import model.Cell;
import service.emulator.ComputationDelayEmulator;
import java.util.List;
import model.Board;
import model.Cell;
import model.MutableBoard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardPartStateServiceTest {
  @Mock ComputationDelayEmulator delayEmulator;

  @Test
  void testMoveToNextStateTriangle() {
    Board board = givenTriangleBoard();
    BoardPartStateService stateService = givenStateServiceForWholeBoard(board);

    stateService.moveToNextState(board);

    assertEquals(expectedTriangleNextStateAlive(), retrieveAliveCells(board));
  }

  @Test
  void testMoveToNextStateToggle() {
    Board board = givenToggleBoard();
    BoardPartStateService stateService = givenStateServiceForWholeBoard(board);

    stateService.moveToNextState(board);

    assertEquals(expectedToggleNextStateAlive(), retrieveAliveCells(board));
  }

  @Test
  void testMoveToNextStateMiddleBoard() {
    Board board = givenMiddleBoard();
    BoardPartStateService stateService = givenStateServiceForWholeBoard(board);

    stateService.moveToNextState(board);

    assertEquals(expectedMiddleBoardNextStateAlive(), retrieveAliveCells(board));
  }

  @Test
  void testCalculateNextStateTriangle() {
    Board board = givenTriangleBoard();

    List<Cell> nextStateAlive = whenCalculatingNextStateAndRetrieveActiveCells(board);

    assertEquals(expectedTriangleNextStateAlive(), nextStateAlive);
  }

  @Test
  void testCalculateNextStateToggle() {
    Board board = givenToggleBoard();

    List<Cell> nextStateAlive = whenCalculatingNextStateAndRetrieveActiveCells(board);

    assertEquals(expectedToggleNextStateAlive(), nextStateAlive);
  }

  @Test
  void testCalculateNextStateMiddleBoard() {
    Board board = givenMiddleBoard();

    List<Cell> nextStateAlive = whenCalculatingNextStateAndRetrieveActiveCells(board);

    assertEquals(expectedMiddleBoardNextStateAlive(), nextStateAlive);
  }

  List<Cell> whenCalculatingNextStateAndRetrieveActiveCells(Board board) {
    BoardPartStateService stateService = givenStateServiceForWholeBoard(board);

    return stateService.calculateNextState(board)
        .filter(ActivatedCell::isActive)
        .map(ActivatedCell::getCell)
        .collect(toList());
  }

  BoardPartStateService givenStateServiceForWholeBoard(Board board) {
    List<Cell> cells =
        Cell.buildCells(board.getNumOfRows(), board.getNumOfColumns()).collect(toList());

    return new BoardPartStateService(cells, delayEmulator);
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
    Board board = new MutableBoard(3, 3);
    board.born(new Cell(0, 1));
    board.born(new Cell(0, 2));
    board.born(new Cell(1, 2));

    return board;
  }

  // · ■ ■
  // · ■ ■
  // · · ·
  private List<Cell> expectedTriangleNextStateAlive() {
    return List.of(
        new Cell(0, 1), new Cell(0, 2),
        new Cell(1, 1), new Cell(1, 2));
  }

  // · ■ ·
  // · ■ ·
  // · ■ ·
  private Board givenToggleBoard() {
    Board board = new MutableBoard(3, 3);
    board.born(new Cell(0, 1));
    board.born(new Cell(1, 1));
    board.born(new Cell(2, 1));

    return board;
  }

  // · · ·
  // ■ ■ ■
  // · · ·
  private List<Cell> expectedToggleNextStateAlive() {
    return range(0, 3).mapToObj(col -> new Cell(1, col)).collect(toList());
  }

  // ■ · ·
  // · · ■
  // ■ · ·
  private Board givenMiddleBoard() {
    Board board = new MutableBoard(3, 3);
    board.born(new Cell(0, 0));
    board.born(new Cell(1, 2));
    board.born(new Cell(2, 0));

    return board;
  }

  // · · ·
  // · ■ ·
  // · · ·
  private List<Cell> expectedMiddleBoardNextStateAlive() {
    return List.of(new Cell(1, 1));
  }
}
