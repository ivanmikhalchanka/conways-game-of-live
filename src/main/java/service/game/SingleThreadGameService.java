package service.game;

import model.CachingBoard;
import renderer.BoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.state.BoardPartStateService;

public class SingleThreadGameService implements GameService {
  private final BoardRenderer renderer;
  private final BoardPartStateService boardPartStateService;
  private final CachingBoard board;

  public SingleThreadGameService(
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
