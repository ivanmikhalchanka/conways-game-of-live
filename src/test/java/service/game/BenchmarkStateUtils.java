package service.game;

import model.CachingBoard;
import model.Cell;
import model.MutableBoard;
import renderer.BoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.emulator.ThreadSleepComputationDelayEmulator;

public class BenchmarkStateUtils {
  public static BoardRenderer givenDefaultBoardRenderer() {
    return board -> {};
  }

  public static ComputationDelayEmulator givenDefaultComputationDelayEmulator() {
    return new ThreadSleepComputationDelayEmulator(1L);
  }

  // · · ■
  // ■ · ■
  // · ■ ■
  public static CachingBoard givenBoardWithGlider() {
    MutableBoard board = new MutableBoard(10, 10);
    board.born(new Cell(0, 2));
    board.born(new Cell(1, 0));
    board.born(new Cell(1, 2));
    board.born(new Cell(2, 1));
    board.born(new Cell(2, 2));

    return new CachingBoard(board);
  }
}
