package factory.game;

import java.util.concurrent.ForkJoinPool;
import lombok.RequiredArgsConstructor;
import model.CachingBoard;
import renderer.BoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.game.FutureGameService;

@RequiredArgsConstructor
public class ForkJoinPoolFutureGameServiceFactory implements FutureGameServiceFactory {
  private final CachingBoard board;
  private final BoardRenderer renderer;
  private final ComputationDelayEmulator delayEmulator;

  @Override
  public FutureGameService build() {
    ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    return new FutureGameService(
        board, renderer, delayEmulator, forkJoinPool, forkJoinPool.getParallelism());
  }
}
