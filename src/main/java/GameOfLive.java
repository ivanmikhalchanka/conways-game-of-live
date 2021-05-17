import factory.GliderBoardFactory;
import model.CachingBoard;
import renderer.BoardRenderer;
import renderer.ConsoleBoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.emulator.ThreadSleepComputationDelayEmulator;
import service.game.FutureGameService;
import service.game.GameService;

public class GameOfLive {
  public static void main(String[] args) {
    var board = new GliderBoardFactory().build(10, 10);
    var cachingBoard = new CachingBoard(board);

    BoardRenderer renderer = new ConsoleBoardRenderer();
    ComputationDelayEmulator delayEmulator = new ThreadSleepComputationDelayEmulator();

    GameService gameService = new FutureGameService(cachingBoard, renderer, delayEmulator);
    gameService.start();
  }
}
