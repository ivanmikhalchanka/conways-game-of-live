package gameoflife.factory.board;

import gameoflife.model.Board;
import gameoflife.model.MutableBoard;
import gameoflife.model.Cell;

public class GliderBoardFactory implements BoardFactory{

  // · · ■
  // ■ · ■
  // · ■ ■
  @Override
  public Board build(int numOfRows, int numOfColumns) {
    var board = new MutableBoard(numOfRows, numOfColumns);
    board.born(new Cell(0, 2));
    board.born(new Cell(1, 0));
    board.born(new Cell(1, 2));
    board.born(new Cell(2, 1));
    board.born(new Cell(2, 2));

    return board;
  }
}
