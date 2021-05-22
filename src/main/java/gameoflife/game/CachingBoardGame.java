package gameoflife.game;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.CachingBoard;
import gameoflife.model.Board;
import gameoflife.renderer.BoardRenderer;

public abstract class CachingBoardGame implements Game {
  protected final CachingBoard board;
  protected final BoardRenderer renderer;
  protected final ComputationDelayEmulator delayEmulator;

  protected CachingBoardGame(Board board, BoardRenderer renderer,
      ComputationDelayEmulator delayEmulator) {
    this.board = new CachingBoard(board);
    this.renderer = renderer;
    this.delayEmulator = delayEmulator;
  }
}
