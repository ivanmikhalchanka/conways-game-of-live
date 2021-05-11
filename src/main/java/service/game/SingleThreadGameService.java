package service.game;

import java.util.HashSet;
import java.util.Set;
import model.Board;
import model.Cell;
import renderer.BoardRenderer;
import service.state.BoardStateService;

public class SingleThreadGameService implements GameService {
  private final Set<Board> history = new HashSet<>();
  private Board board;

  private final BoardRenderer renderer;

  public SingleThreadGameService(Board board, BoardRenderer renderer) {
    this.board = board;
    this.renderer = renderer;
  }

  @Override
  public void start() {
    while (!gameOver(board)) {
      history.add(board);
      renderer.render(board);

      board = new BoardStateService(board).calculateNextState();
    }
  }

  boolean gameOver(Board board) {
    return Cell.buildCells(board.getNumOfRows(), board.getNumOfColumns()).noneMatch(board::isAlive)
        || history.contains(board);
  }
}
