import model.Board;
import renderer.ConsoleBoardRenderer;

public class GameOfLive {
  public static void main(String[] args) {
    Board board = new Board(5, 5);

    new ConsoleBoardRenderer().render(board);
  }
}
