package service.game;

import static service.game.BenchmarkStateUtils.givenBoardWithGlider;
import static service.game.BenchmarkStateUtils.givenDefaultBoardRenderer;
import static service.game.BenchmarkStateUtils.givenDefaultComputationDelayEmulator;

import java.util.concurrent.ForkJoinPool;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class ForkJoinExecutionPlan {
  public GameService gameService;

  @Setup(Level.Invocation)
  public void setupGameService() {
    ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    gameService =
        new FutureGameService(
            givenBoardWithGlider(),
            givenDefaultBoardRenderer(),
            givenDefaultComputationDelayEmulator(),
            forkJoinPool,
            forkJoinPool.getParallelism());
  }

  @TearDown
  public void forceGC() {
    System.gc();
  }
}
