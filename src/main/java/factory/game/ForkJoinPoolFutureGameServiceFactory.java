package factory.game;

import java.util.concurrent.ForkJoinPool;
import lombok.RequiredArgsConstructor;
import model.CachingBoard;
import renderer.BoardRenderer;
import emulator.ComputationDelayEmulator;
import game.FutureGame;

@RequiredArgsConstructor
public class ForkJoinPoolFutureGameServiceFactory implements FutureGameServiceFactory {
  private final CachingBoard board;
  private final BoardRenderer renderer;
  private final ComputationDelayEmulator delayEmulator;

  @Override
  public FutureGame build() {
    ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    return new FutureGame(
        board, renderer, delayEmulator, forkJoinPool, forkJoinPool.getParallelism());
  }
}
