import model.CachingBoard;
import service.emulator.ComputationDelayEmulator;
import service.emulator.ThreadSleepComputationDelayEmulator;
import factory.GliderBoardFactory;
import renderer.BoardRenderer;
import renderer.ConsoleBoardRenderer;
import service.game.GameService;
import service.game.ThreadPoolGameService;

public class GameOfLive {
  public static void main(String[] args) {
    var board = new GliderBoardFactory().build(10, 10);
    var cachingBoard = new CachingBoard(board);

    BoardRenderer renderer = new ConsoleBoardRenderer();
    ComputationDelayEmulator delayEmulator = new ThreadSleepComputationDelayEmulator();

    GameService gameService = new ThreadPoolGameService(cachingBoard, renderer, delayEmulator);
    gameService.start();
  }
}
