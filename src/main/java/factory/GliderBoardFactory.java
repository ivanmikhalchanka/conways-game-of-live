package factory;

import model.Board;
import model.Cell;

public class GliderBoardFactory implements BoardFactory{

  // · · ■
  // ■ · ■
  // · ■ ■
  @Override
  public Board build(int numOfRows, int numOfColumns) {
    Board board = new Board(numOfRows, numOfColumns);
    board.born(new Cell(0, 2));
    board.born(new Cell(1, 0));
    board.born(new Cell(1, 2));
    board.born(new Cell(2, 1));
    board.born(new Cell(2, 2));

    return board;
  }
}
