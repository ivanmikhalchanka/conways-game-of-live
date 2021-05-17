package model;

import java.util.Objects;

public class ActivatedCell {
  private final Cell cell;
  private final boolean active;

  public ActivatedCell(Cell cell, boolean active) {
    this.cell = cell;
    this.active = active;
  }

  public boolean isActive() {
    return active;
  }

  public Cell getCell() {
    return cell;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActivatedCell that = (ActivatedCell) o;
    return active == that.active && Objects.equals(cell, that.cell);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cell, active);
  }

  @Override
  public String toString() {
    return "ActivatedCell{" +
        "cell=" + cell +
        ", active=" + active +
        '}';
  }
}
