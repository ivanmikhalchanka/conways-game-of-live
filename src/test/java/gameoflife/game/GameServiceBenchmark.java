package gameoflife.game;

import static gameoflife.game.BenchmarkStateUtils.givenBoardWithGlider;
import static gameoflife.game.BenchmarkStateUtils.givenDefaultBoardRenderer;
import static gameoflife.game.BenchmarkStateUtils.givenDefaultComputationDelayEmulator;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(
    value = 1,
    jvmArgs = {"-Xms4G", "-Xmx4G"})
public class GameServiceBenchmark {
  @Benchmark
  public void singleThreadBenchmark() {
    SingleThreadGame.start(
        givenBoardWithGlider(),
        givenDefaultBoardRenderer(),
        givenDefaultComputationDelayEmulator());
  }

  @Benchmark
  public void cyclicBarrierBenchmark() {
    CyclicBarrierGame.start(
        givenBoardWithGlider(),
        givenDefaultBoardRenderer(),
        givenDefaultComputationDelayEmulator());
  }

  @Benchmark
  public void threadPoolBenchmark() {
    ThreadPoolGame.start(
        givenBoardWithGlider(),
        givenDefaultBoardRenderer(),
        givenDefaultComputationDelayEmulator());
  }

  @Benchmark
  public void forkJoinPoolBenchmark() {
    ForkJoinPoolGame.start(
        givenBoardWithGlider(),
        givenDefaultBoardRenderer(),
        givenDefaultComputationDelayEmulator());
  }

  @Benchmark
  public void parallelStreamBenchmark() {
    ParallelStreamGame.start(
        givenBoardWithGlider(),
        givenDefaultBoardRenderer(),
        givenDefaultComputationDelayEmulator());
  }

  @Benchmark
  public void completableFutureBenchmark() {
    CompletableFutureGame.start(
        givenBoardWithGlider(),
        givenDefaultBoardRenderer(),
        givenDefaultComputationDelayEmulator());
  }
}
