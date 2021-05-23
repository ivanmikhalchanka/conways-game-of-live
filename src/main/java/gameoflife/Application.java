package gameoflife;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.emulator.ThreadSleepComputationDelayEmulator;
import gameoflife.factory.board.GliderBoardFactory;
import gameoflife.game.ForkJoinPoolGame;
import gameoflife.renderer.BoardRenderer;
import gameoflife.renderer.PrintStreamBoardRenderer;

public class Application {
  public static void main(String[] args) {
    var board = new GliderBoardFactory().build(10, 10);

    BoardRenderer renderer = new PrintStreamBoardRenderer(System.out);
    ComputationDelayEmulator delayEmulator = new ThreadSleepComputationDelayEmulator(5L);

    ForkJoinPoolGame.start(board, renderer, delayEmulator);
  }
}
