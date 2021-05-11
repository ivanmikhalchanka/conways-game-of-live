package model;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CachingBoard implements Board {
  private final List<SimpleBoard> history = new ArrayList<>();
  private final SimpleBoard uncommittedChanges;

  private SimpleBoard board;

  public CachingBoard(Board board) {
    this.uncommittedChanges = new SimpleBoard(board);
    this.commitChanges();
  }

  @Override
  public int getNumOfRows() {
    return board.getNumOfRows();
  }

  @Override
  public int getNumOfColumns() {
    return board.getNumOfColumns();
  }

  @Override
  public void born(Cell cell) {
    uncommittedChanges.born(cell);
  }

  @Override
  public void kill(Cell cell) {
    uncommittedChanges.kill(cell);
  }

  @Override
  public boolean isAlive(Cell cell) {
    return board.isAlive(cell);
  }

  @Override
  public List<Cell> getNeighbours(Cell cell) {
    return board.getNeighbours(cell);
  }

  public void commitChanges() {
    board = new SimpleBoard(this.uncommittedChanges);
    history.add(board);
  }

  public List<Cell> getPartToProcess(int total, int index) {
    int totalCells = getNumOfRows() * getNumOfColumns();
    int cellsPerPartition = totalCells / total;

    Stream<Cell> cells =
        Cell.buildCells(getNumOfRows(), getNumOfColumns()).skip((long) index * cellsPerPartition);

    return total > index + 1
        ? cells.limit(cellsPerPartition).collect(toList())
        : cells.collect(toList());
  }

  public boolean hasConverged() {
    return allDead() || stateRepeated();
  }

  boolean allDead() {
    return Cell.buildCells(getNumOfRows(), getNumOfColumns())
        .noneMatch(uncommittedChanges::isAlive);
  }

  boolean stateRepeated() {
    return history.stream().limit(history.size() - 1).collect(toSet()).contains(uncommittedChanges);
  }
}
