package game;

import static game.BenchmarkStateUtils.givenBoardWithGlider;
import static game.BenchmarkStateUtils.givenDefaultBoardRenderer;
import static game.BenchmarkStateUtils.givenDefaultComputationDelayEmulator;

import java.util.concurrent.ForkJoinPool;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class ForkJoinExecutionPlan {
  public Game game;

  @Setup(Level.Invocation)
  public void setupGameService() {
    ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    game =
        new FutureGame(
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
