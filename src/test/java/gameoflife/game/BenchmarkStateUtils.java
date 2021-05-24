package gameoflife.game;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.emulator.ThreadSleepComputationDelayEmulator;
import gameoflife.model.MutableBoard;
import gameoflife.model.Board;
import gameoflife.model.Cell;
import gameoflife.renderer.BoardRenderer;

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
  public static Board givenBoardWithGlider() {
    MutableBoard board = new MutableBoard(10, 10);
    board.born(new Cell(0, 2));
    board.born(new Cell(1, 0));
    board.born(new Cell(1, 2));
    board.born(new Cell(2, 1));
    board.born(new Cell(2, 2));

    return board;
  }
}
