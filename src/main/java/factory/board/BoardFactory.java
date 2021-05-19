package factory.board;

import model.Board;

public interface BoardFactory {
  Board build(int numOfRows, int numOfColumns);
}
