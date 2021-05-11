package model;

import java.util.List;

public interface Board {
  int getNumOfRows();

  int getNumOfColumns();

  void born(Cell cell);

  void kill(Cell cell);

  boolean isAlive(Cell cell);

  List<Cell> getNeighbours(Cell cell);
}
