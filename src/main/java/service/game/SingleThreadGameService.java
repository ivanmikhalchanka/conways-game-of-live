package service.game;

import model.Board;
import model.CachingBoard;
import renderer.BoardRenderer;
import service.state.BoardPartStateService;

public class SingleThreadGameService implements GameService {
  private final BoardRenderer renderer;
  private final BoardPartStateService boardPartStateService;
  private final CachingBoard board;

  public SingleThreadGameService(Board board, BoardRenderer renderer) {
    this.board = new CachingBoard(board);
    this.renderer = renderer;
    this.boardPartStateService = new BoardPartStateService(this.board.getPartToProcess(1, 0));
  }

  @Override
  public void start() {
    while (!board.hasConverged()) {
      board.commitChanges();
      renderer.render(board);

      boardPartStateService.moveToNextState(board);
    }
  }
}
