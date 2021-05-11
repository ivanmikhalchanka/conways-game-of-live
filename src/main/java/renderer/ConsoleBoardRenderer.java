package renderer;

import model.Board;
import model.Cell;

public class ConsoleBoardRenderer implements BoardRenderer {
  public final static char DEAD = '·';
  public final static char ALIVE = '■';

  @Override
  public void render(Board board) {
    for (int row = 0; row < board.getNumOfRows(); row++) {
      Cell.buildCellsRow(row, board.getNumOfColumns())
          .map(cell -> board.isAlive(cell) ? ALIVE : DEAD)
          .forEach(symbol -> System.out.printf("%s ", symbol));

      System.out.println();
    }
    System.out.println();
  }
}
