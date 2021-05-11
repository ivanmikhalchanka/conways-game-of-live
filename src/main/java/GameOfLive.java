import factory.GliderBoardFactory;
import model.Board;
import renderer.BoardRenderer;
import renderer.ConsoleBoardRenderer;
import service.game.GameService;
import service.game.ThreadPoolGameService;

public class GameOfLive {
  public static void main(String[] args) {
    Board board = new GliderBoardFactory().build(10, 10);

    BoardRenderer renderer = new ConsoleBoardRenderer();

    GameService gameService = new ThreadPoolGameService(board, renderer);
    gameService.start();
  }
}
