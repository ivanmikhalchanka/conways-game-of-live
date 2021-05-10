package model;

public class Board {
  private final boolean[][] cells;

  public Board(int numOfRows, int numOfColumns) {
    cells = new boolean[numOfRows][numOfColumns];
  }

  public int getNumOfRows() {
    return cells.length;
  }

  public int getNumOfColumns() {
    return cells[0].length;
  }

  public void born(int row, int column) {
    cells[row][column] = true;
  }

  public void kill(int row, int column) {
    cells[row][column] = false;
  }

  public boolean isAlive(int row, int column) {
    return cells[row][column];
  }
}
