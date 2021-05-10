import factory.GliderBoardFactory;
import model.Board;
import renderer.ConsoleBoardRenderer;
import service.SingleThreadGameService;

public class GameOfLive {
  public static void main(String[] args) {
    Board board = new GliderBoardFactory().build(10, 10);

    ConsoleBoardRenderer renderer = new ConsoleBoardRenderer();

    SingleThreadGameService gameService = new SingleThreadGameService(renderer);
    gameService.start(board);
  }
}
