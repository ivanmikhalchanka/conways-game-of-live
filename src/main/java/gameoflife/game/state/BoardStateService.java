package gameoflife.game.state;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.model.Board;
import gameoflife.model.Cell;

public class BoardStateService {
  public static final int BORN_THRESHOLD = 3;
  public static final int STAY_ALIVE = 2;

  private final ComputationDelayEmulator delayEmulator;

  public BoardStateService(ComputationDelayEmulator delayEmulator) {
    this.delayEmulator = delayEmulator;
  }

  public boolean shouldBeAlive(Cell cell, Board board) {
    this.delayEmulator.emulateComputationDelay(1);

    long aliveNeighbours = board.getNeighbours(cell).stream().filter(board::isAlive).count();

    return board.isAlive(cell) ? shouldStayAlive(aliveNeighbours) : shouldBorn(aliveNeighbours);
  }

  boolean shouldStayAlive(long aliveNeighbours) {
    return aliveNeighbours >= STAY_ALIVE && aliveNeighbours <= BORN_THRESHOLD;
  }

  boolean shouldBorn(long aliveNeighbours) {
    return aliveNeighbours == BORN_THRESHOLD;
  }
}
