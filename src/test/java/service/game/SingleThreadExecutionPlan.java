package service.game;

import static service.game.BenchmarkStateUtils.givenBoardWithGlider;
import static service.game.BenchmarkStateUtils.givenDefaultBoardRenderer;
import static service.game.BenchmarkStateUtils.givenDefaultComputationDelayEmulator;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class SingleThreadExecutionPlan {
  public GameService gameService;

  @Setup(Level.Invocation)
  public void setupGameService() {
    gameService =
        new SingleThreadGameService(
            givenBoardWithGlider(),
            givenDefaultBoardRenderer(),
            givenDefaultComputationDelayEmulator());
  }

  @TearDown
  public void forceGC() {
    System.gc();
  }
}
