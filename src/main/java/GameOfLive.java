import factory.board.GliderBoardFactory;
import factory.game.FixedThreadPoolFutureGameServiceFactory;
import factory.game.ForkJoinPoolFutureGameServiceFactory;
import model.CachingBoard;
import renderer.BoardRenderer;
import renderer.ConsoleBoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.emulator.ThreadSleepComputationDelayEmulator;
import service.game.GameService;

public class GameOfLive {
  public static void main(String[] args) {
    var board = new GliderBoardFactory().build(10, 10);
    var cachingBoard = new CachingBoard(board);

    BoardRenderer renderer = new ConsoleBoardRenderer();
    ComputationDelayEmulator delayEmulator = new ThreadSleepComputationDelayEmulator();

    GameService gameService =
        new ForkJoinPoolFutureGameServiceFactory(cachingBoard, renderer, delayEmulator).build();
    gameService.start();
  }
}
