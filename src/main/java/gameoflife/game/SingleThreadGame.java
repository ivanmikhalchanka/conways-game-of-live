package gameoflife.game;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.BoardPartStateService;
import gameoflife.model.Board;
import gameoflife.game.state.CachingBoard;
import gameoflife.renderer.BoardRenderer;

public class SingleThreadGame implements Game {
  private final BoardRenderer renderer;
  private final BoardPartStateService boardPartStateService;
  private final CachingBoard board;

  public SingleThreadGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    this.board = new CachingBoard(board);
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
