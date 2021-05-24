#### Description

Concurrency playground project based
on [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life)

#### Game implementations

- `SingleThreadGame` - all calculations complete in a single thread, no cuncurrency used at all

- `CyclicBarrierGame` - using plain old `Threads` with `CyclicBarrier` synchronization between
  iterations. Fixed number of threads start, each of them responsible for part of board. Threads run
  until game over.

- `ThreadPoolGame` - use `FixedThreadPool` (**Work Arbitrage** approach based on **blocking queue**)

- `ForkJoinPoolGame` - use `ForkJoinPool` (**Work Stealing** approach: each Thread have number of
  tasks and can steal tasks from other threads once all own tasks completed) with implementation
  of `RecursiveTask` that split cells into batches based on the number of cores

- `ParallelStreamGame` - use delay on per-cell basis instead of per-batch.

- `CompletableFutureGame` - execute tasks asynchronously wrapping them up with `CompletableFuture`
  and executing through `CompletableFuture.supplyAsync` using common fork-join pool

#### Benchmarks

##### Without delay

| Benchmark       | Mode | Cnt | Score   | Error | Units |
|-----------------|------|-----|---------|-------|------ |
| SingleThread    | avgt |   5 | 3.830 ± | 0.049 | ms/op |
| CyclicBarrier   | avgt |   5 | 2.202 ± | 0.686 | ms/op |
| ThreadPool      | avgt |   5 | 6.073 ± | 4.685 | ms/op |
| ForkJoinPool    | avgt |   5 | 5.755 ± | 0.019 | ms/op |

##### With number of cells to process by task delay

| Benchmark         | Mode | Cnt | Score      | Error   | Units |
|-------------------|------|-----|------------|---------|-------|
| SingleThread      | avgt |   5 | 3370.461 ± | 204.707 | ms/op |
| CyclicBarrier     | avgt |   5 |    6.751 ± |   0.316 | ms/op |
| ThreadPool        | avgt |   5 |  392.589 ± |   4.785 | ms/op |
| ForkJoinPool      | avgt |   5 |  362.661 ± |  19.011 | ms/op |
| ParallelStream    | avgt |   5 |  420.998 ± |   8.882 | ms/op |
| CompletableFuture | avgt |   5 |  391.423 ± |  16.021 | ms/op |
