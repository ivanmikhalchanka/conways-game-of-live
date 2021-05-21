package gameoflife.factory.board;

import gameoflife.model.Board;

public interface BoardFactory {
  Board build(int numOfRows, int numOfColumns);
}
