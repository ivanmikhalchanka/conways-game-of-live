package factory;

import model.Board;

public interface BoardFactory {
  Board build(int numOfRows, int numOfColumns);
}
