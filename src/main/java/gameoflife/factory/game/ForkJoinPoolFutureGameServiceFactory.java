package gameoflife.factory.game;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.FutureGame;
import java.util.concurrent.ForkJoinPool;
import lombok.RequiredArgsConstructor;
import gameoflife.model.Board;
import gameoflife.renderer.BoardRenderer;

@RequiredArgsConstructor
public class ForkJoinPoolFutureGameServiceFactory implements FutureGameServiceFactory {
  private final Board board;
  private final BoardRenderer renderer;
  private final ComputationDelayEmulator delayEmulator;

  @Override
  public FutureGame build() {
    var forkJoinPool = ForkJoinPool.commonPool();

    return new FutureGame(
        board, renderer, delayEmulator, forkJoinPool, forkJoinPool.getParallelism());
  }
}
