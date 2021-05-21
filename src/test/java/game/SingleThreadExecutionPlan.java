package game;

import static game.BenchmarkStateUtils.givenBoardWithGlider;
import static game.BenchmarkStateUtils.givenDefaultBoardRenderer;
import static game.BenchmarkStateUtils.givenDefaultComputationDelayEmulator;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class SingleThreadExecutionPlan {
  public Game game;

  @Setup(Level.Invocation)
  public void setupGameService() {
    game =
        new SingleThreadGame(
            givenBoardWithGlider(),
            givenDefaultBoardRenderer(),
            givenDefaultComputationDelayEmulator());
  }

  @TearDown
  public void forceGC() {
    System.gc();
  }
}
