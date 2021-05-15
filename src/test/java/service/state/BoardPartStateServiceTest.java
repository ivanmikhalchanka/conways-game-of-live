package service.state;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.*;

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
  @Mock
  ComputationDelayEmulator delayEmulator;

  @Test
  void testCalculateNextStateTriangle() {
    Board board = givenTriangleBoard();
    BoardPartStateService stateService = givenStateServiceForWholeBoard(board);

    stateService.moveToNextState(board);

    List<Cell> aliveCells = retrieveAliveCells(board);
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

    stateService.moveToNextState(board);

    List<Cell> aliveCells = retrieveAliveCells(board);
    List<Cell> expectedAlive = range(0, 3).mapToObj(col -> new Cell(1, col)).collect(toList());
    assertEquals(expectedAlive, aliveCells);
  }

  @Test
  void testCalculateNextStateMiddleBoard() {
    Board board = givenMiddleBoard();
    BoardPartStateService stateService = givenStateServiceForWholeBoard(board);

    stateService.moveToNextState(board);

    assertEquals(List.of(new Cell(1, 1)), retrieveAliveCells(board));
  }

  BoardPartStateService givenStateServiceForWholeBoard(Board board) {
    List<Cell> cells = Cell.buildCells(board.getNumOfRows(), board.getNumOfColumns())
        .collect(toList());

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
}
