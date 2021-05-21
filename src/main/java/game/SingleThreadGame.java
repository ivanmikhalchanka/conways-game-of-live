package game;

import game.Game;
import model.CachingBoard;
import renderer.BoardRenderer;
import emulator.ComputationDelayEmulator;
import game.state.BoardPartStateService;

public class SingleThreadGame implements Game {
  private final BoardRenderer renderer;
  private final BoardPartStateService boardPartStateService;
  private final CachingBoard board;

  public SingleThreadGame(
      CachingBoard board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    this.board = board;
    this.renderer = renderer;
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
