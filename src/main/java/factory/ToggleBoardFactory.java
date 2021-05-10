package factory;

import model.Board;
import model.Cell;

public class ToggleBoardFactory implements BoardFactory {

  // · ■ ·
  // · ■ ·
  // · ■ ·
  @Override
  public Board build(int numOfRows, int numOfColumns) {
    Board board = new Board(numOfRows, numOfColumns);
    board.born(new Cell(0, 1));
    board.born(new Cell(1, 1));
    board.born(new Cell(2, 1));

    return board;
  }
}
