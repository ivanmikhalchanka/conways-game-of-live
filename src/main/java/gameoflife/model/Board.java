package gameoflife.model;

import java.util.List;

public interface Board {
  int getNumOfRows();

  int getNumOfColumns();

  void born(Cell cell);

  void kill(Cell cell);

  boolean isAlive(Cell cell);

  List<Cell> getAllCells();

  List<Cell> getPartOfCells(int total, int index);

  List<Cell> getNeighbours(Cell cell);
}
