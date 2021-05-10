package renderer;

import model.Board;
import model.Cell;

public class ConsoleBoardRenderer implements BoardRenderer {
  public final static char DEAD = '·';
  public final static char ALIVE = '■';

  public void render(Board board) {
    for (int row = 0; row < board.getNumOfRows(); row++) {
      for (int column = 0; column < board.getNumOfColumns(); column++) {
        char symbol = board.isAlive(new Cell(row, column)) ? ALIVE : DEAD;

        System.out.printf("%s ", symbol);
      }
      System.out.println();
    }
    System.out.println();
  }
}
