package gameoflife.renderer;

import gameoflife.model.Board;
import gameoflife.model.Cell;
import java.io.PrintStream;

public class PrintStreamBoardRenderer implements BoardRenderer {
  public static final char DEAD = '·';
  public static final char ALIVE = '■';

  private final PrintStream out;

  public PrintStreamBoardRenderer(PrintStream out) {
    this.out = out;
  }

  @Override
  public void render(Board board) {
    for (var row = 0; row < board.getNumOfRows(); row++) {
      Cell.buildCellsRow(row, board.getNumOfColumns())
          .map(cell -> board.isAlive(cell) ? ALIVE : DEAD)
          .forEach(symbol -> out.printf("%s ", symbol));

      out.println();
    }
    out.println();
  }
}
