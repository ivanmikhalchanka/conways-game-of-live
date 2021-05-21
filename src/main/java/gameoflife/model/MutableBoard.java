package gameoflife.model;

import static gameoflife.common.IntPredicates.isPositive;
import static gameoflife.common.IntPredicates.lessThan;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MutableBoard implements Board {
  private final boolean[][] cells;

  public MutableBoard(int numOfRows, int numOfColumns) {
    cells = new boolean[numOfRows][numOfColumns];
  }

  public MutableBoard(Board board) {
    cells = new boolean[board.getNumOfRows()][board.getNumOfColumns()];

    Cell.buildCells(board.getNumOfRows(), board.getNumOfColumns())
        .filter(board::isAlive)
        .forEach(this::born);
  }

  @Override
  public int getNumOfRows() {
    return cells.length;
  }

  @Override
  public int getNumOfColumns() {
    return cells[0].length;
  }

  @Override
  public void born(Cell cell) {
    cells[cell.getRow()][cell.getCol()] = true;
  }

  @Override
  public void kill(Cell cell) {
    cells[cell.getRow()][cell.getCol()] = false;
  }

  @Override
  public boolean isAlive(Cell cell) {
    return cells[cell.getRow()][cell.getCol()];
  }

  @Override
  public List<Cell> getNeighbours(Cell cell) {
    List<Integer> rowIndexes = calculateNeighbourIndexes(cell.getRow(), getNumOfRows());
    List<Integer> columnIndexes = calculateNeighbourIndexes(cell.getCol(), getNumOfColumns());

    return rowIndexes.stream()
        .flatMap(row -> buildRowCells(row, columnIndexes))
        .filter(not(cell::equals))
        .collect(toList());
  }

  @Override
  public List<Cell> getAllCells() {
    return getPartOfCells(1, 0);
  }

  @Override
  public List<Cell> getPartOfCells(int total, int index) {
    int totalCells = getNumOfRows() * getNumOfColumns();
    int cellsPerPartition = totalCells / total;

    Stream<Cell> cellsTail =
        Cell.buildCells(getNumOfRows(), getNumOfColumns()).skip((long) index * cellsPerPartition);

    return total > index + 1
        ? cellsTail.limit(cellsPerPartition).collect(toList())
        : cellsTail.collect(toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MutableBoard board = (MutableBoard) o;
    return Arrays.deepEquals(cells, board.cells);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(cells);
  }

  Stream<Cell> buildRowCells(int row, List<Integer> columns) {
    return columns.stream().map(col -> new Cell(row, col));
  }

  List<Integer> calculateNeighbourIndexes(int index, int bound) {
    return IntStream.rangeClosed(index - 1, index + 1)
        .filter(isPositive())
        .filter(lessThan(bound))
        .boxed()
        .collect(toList());
  }
}
