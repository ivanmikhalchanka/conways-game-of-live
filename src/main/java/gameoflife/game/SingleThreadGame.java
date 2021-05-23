package gameoflife.game;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.BoardPartStateService;
import gameoflife.model.Board;
import gameoflife.renderer.BoardRenderer;

public class SingleThreadGame extends CachingBoardGame {
  private final BoardPartStateService boardPartStateService;

  public static void start(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    new SingleThreadGame(board, renderer, delayEmulator).start();
  }

  private SingleThreadGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    super(board, renderer, delayEmulator);

    this.boardPartStateService = new BoardPartStateService(this.board.getAllCells(), delayEmulator);
  }

  @Override
  public void start() {
    super.applyChangesUtilConverged(() -> boardPartStateService.calculateNextState(board));
  }
}
