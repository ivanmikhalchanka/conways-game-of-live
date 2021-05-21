package game;

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
  public void singleThreadBenchmark(SingleThreadExecutionPlan executionPlan) {
    executionPlan.game.start();
  }

  @Benchmark
  public void cyclicBarrierBenchmark(CyclicBarrierExecutionPlan executionPlan) {
    executionPlan.game.start();
  }

  @Benchmark
  public void forkJoinBenchmark(ForkJoinExecutionPlan executionPlan) {
    executionPlan.game.start();
  }

  @Benchmark
  public void fixedThreadPoolBenchmark(FixedThreadPoolExecutionPlan executionPlan) {
    executionPlan.game.start();
  }
}
