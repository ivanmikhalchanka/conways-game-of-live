package gameoflife.game;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.ActivatedCell;
import gameoflife.game.state.BoardStateService;
import gameoflife.model.Board;
import gameoflife.renderer.BoardRenderer;
import java.util.stream.Stream;

public class ParallelStreamGame extends CachingBoardGame {
  private final BoardStateService stateService;

  public static void start(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    new ParallelStreamGame(board, renderer, delayEmulator).start();
  }

  private ParallelStreamGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    super(board, renderer, delayEmulator);

    stateService = new BoardStateService(delayEmulator);
  }

  @Override
  public void start() {
    super.applyChangesUtilConverged(this::calculateChanges);
  }

  Stream<ActivatedCell> calculateChanges() {
    return board.getAllCells().parallelStream()
        .map(cell -> new ActivatedCell(cell, stateService.shouldBeAlive(cell, board)));
  }
}
