package gameoflife.game;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.BoardPartStateService;
import gameoflife.model.Board;
import gameoflife.game.state.CachingBoard;
import gameoflife.renderer.BoardRenderer;

public class SingleThreadGame extends CachingBoardGame {
  private final BoardPartStateService boardPartStateService;

  public SingleThreadGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    super(board, renderer, delayEmulator);

    this.boardPartStateService = new BoardPartStateService(this.board.getAllCells(), delayEmulator);
  }

  @Override
  public void start() {
    while (!board.hasConverged()) {
      boardPartStateService.moveToNextState(board);
      board.commitChanges();

      renderer.render(board);
    }
  }
}
