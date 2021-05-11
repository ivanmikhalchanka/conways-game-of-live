package model;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CachingBoard extends Board {
  private final List<Board> history = new ArrayList<>();

  private final Board cache;

  public CachingBoard(Board board) {
    super(board.getNumOfRows(), board.getNumOfColumns());
    cache = board;
    this.commitNewValues();
  }

  @Override
  public void born(Cell cell) {
    cache.born(cell);
  }

  @Override
  public void kill(Cell cell) {
    cache.kill(cell);
  }

  public void commitNewValues() {
    history.add(new Board(cache));

    Map<Boolean, List<Cell>> cellsByAliveFlag =
        Cell.buildCells(getNumOfRows(), getNumOfColumns())
            .collect(Collectors.partitioningBy(cache::isAlive));

    cellsByAliveFlag.get(true).forEach(super::born);
    cellsByAliveFlag.get(false).forEach(super::kill);
  }

  public boolean hasConverged() {
    return Cell.buildCells(getNumOfRows(), getNumOfColumns()).noneMatch(cache::isAlive)
        || history.stream().limit(history.size() - 1).collect(toSet()).contains(cache);
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
}
