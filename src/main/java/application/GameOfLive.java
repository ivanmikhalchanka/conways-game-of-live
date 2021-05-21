package application;

import factory.board.GliderBoardFactory;
import factory.game.ForkJoinPoolFutureGameServiceFactory;
import model.CachingBoard;
import renderer.BoardRenderer;
import renderer.ConsoleBoardRenderer;
import emulator.ComputationDelayEmulator;
import emulator.ThreadSleepComputationDelayEmulator;
import game.Game;

public class GameOfLive {
  public static void main(String[] args) {
    var board = new GliderBoardFactory().build(10, 10);
    var cachingBoard = new CachingBoard(board);

    BoardRenderer renderer = new ConsoleBoardRenderer();
    ComputationDelayEmulator delayEmulator = new ThreadSleepComputationDelayEmulator(5L);

    Game game =
        new ForkJoinPoolFutureGameServiceFactory(cachingBoard, renderer, delayEmulator).build();
    game.start();
  }
}
