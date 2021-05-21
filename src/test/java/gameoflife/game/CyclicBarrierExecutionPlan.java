package gameoflife.game;

import static gameoflife.game.BenchmarkStateUtils.givenBoardWithGlider;
import static gameoflife.game.BenchmarkStateUtils.givenDefaultBoardRenderer;
import static gameoflife.game.BenchmarkStateUtils.givenDefaultComputationDelayEmulator;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class CyclicBarrierExecutionPlan {
  public Game game;

  @Setup(Level.Invocation)
  public void setupGameService() {
    game =
        new CyclicBarrierGame(
            givenBoardWithGlider(),
            givenDefaultBoardRenderer(),
            givenDefaultComputationDelayEmulator());
  }

  @TearDown
  public void forceGC() {
    System.gc();
  }
}
