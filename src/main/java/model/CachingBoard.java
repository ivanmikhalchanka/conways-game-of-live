package model;

import static java.util.stream.Collectors.toSet;

import game.state.MutableBoard;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.Delegate;

public class CachingBoard implements Board {
  private interface Mutations {
    void born(Cell cell);

    void kill(Cell cell);
  }

  private final List<MutableBoard> history = new ArrayList<>();
  private final MutableBoard uncommittedChanges;

  @Delegate(excludes = Mutations.class)
  private MutableBoard board;

  public CachingBoard(Board board) {
    this.uncommittedChanges = new MutableBoard(board);
    this.commitChanges();
  }

  @Override
  public void born(Cell cell) {
    uncommittedChanges.born(cell);
  }

  @Override
  public void kill(Cell cell) {
    uncommittedChanges.kill(cell);
  }

  public void commitChanges() {
    board = new MutableBoard(this.uncommittedChanges);
    history.add(board);
  }

  public boolean hasConverged() {
    return allDead() || stateRepeated();
  }

  boolean allDead() {
    return Cell.buildCells(getNumOfRows(), getNumOfColumns())
        .noneMatch(uncommittedChanges::isAlive);
  }

  boolean stateRepeated() {
    return history.stream()
        .limit(history.size() - 1L)
        .collect(toSet())
        .contains(uncommittedChanges);
  }
}
