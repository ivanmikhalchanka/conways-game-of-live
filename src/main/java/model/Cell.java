package model;

import static java.util.stream.IntStream.range;

import java.util.Objects;
import java.util.stream.Stream;

public class Cell {
  private final int row;
  private final int col;

  public static Stream<Cell> buildCells(int numOfRows, int numOfCols) {
    return range(0, numOfRows).boxed().flatMap(row -> buildCellsRow(row, numOfCols));
  }

  static Stream<Cell> buildCellsRow(int row, int numOfCols) {
    return range(0, numOfCols).mapToObj(col -> new Cell(row, col));
  }

  public Cell(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cell cell = (Cell) o;
    return row == cell.row && col == cell.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  @Override
  public String toString() {
    return "Cell{" +
        "row=" + row +
        ", col=" + col +
        '}';
  }
}
