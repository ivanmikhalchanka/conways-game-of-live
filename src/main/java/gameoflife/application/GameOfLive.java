package gameoflife.application;

import gameoflife.factory.board.GliderBoardFactory;
import gameoflife.factory.game.ForkJoinPoolFutureGameServiceFactory;
import gameoflife.renderer.BoardRenderer;
import gameoflife.renderer.PrintStreamBoardRenderer;
import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.emulator.ThreadSleepComputationDelayEmulator;
import gameoflife.game.Game;

public class GameOfLive {
  public static void main(String[] args) {
    var board = new GliderBoardFactory().build(10, 10);

    BoardRenderer renderer = new PrintStreamBoardRenderer(System.out);
    ComputationDelayEmulator delayEmulator = new ThreadSleepComputationDelayEmulator(5L);

    Game game = new ForkJoinPoolFutureGameServiceFactory(board, renderer, delayEmulator).build();
    game.start();
  }
}
