package service.game;

import static service.game.BenchmarkStateUtils.givenBoardWithGlider;
import static service.game.BenchmarkStateUtils.givenDefaultBoardRenderer;
import static service.game.BenchmarkStateUtils.givenDefaultComputationDelayEmulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class FixedThreadPoolExecutionPlan {
  public GameService gameService;

  @Setup(Level.Invocation)
  public void setupGameService() {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    int availableThreads = availableProcessors - 1;

    ExecutorService threadPool = Executors.newFixedThreadPool(availableThreads);

    gameService =
        new FutureGameService(
            givenBoardWithGlider(),
            givenDefaultBoardRenderer(),
            givenDefaultComputationDelayEmulator(),
            threadPool,
            availableThreads);
  }

  @TearDown
  public void forceGC() {
    System.gc();
  }
}
