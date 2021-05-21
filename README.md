#### Description

Concurrency playground project based
on [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life)

#### Game implementations

- `CyclicBarrierGameService` - using plain old `Threads` with `CyclicBarrier` synchronization
  between iterations. Fixed number of threads start, each of them responsible for part of board.
  Threads run until game over.

- `FutureGameService` - designed for using `ExecutorService`. Create new tasks for each iteration,
  wait for all task completion, combine result and check whether game is over.

    - `FixedThreadPoolFutureGameServiceFactory` - creates `FutureGameService` with `FixedThreadPool`
      .
      `FixedThreadPool` use **Work Arbitrage** approach based on **blocking queue**

    - `ForkJoinPoolFutureGameServiceFactory` - creates `FutureGameService` with `ForkJoinPool`.
      `ForkJoinPool` use **Work Stealing** approach: each Thread have number of tasks and can steal
      tasks from other threads once all own tasks completed.

#### Benchmarks
##### Without delay
| Benchmark       | Mode | Cnt | Score   | Error | Units |
|-----------------|------|-----|---------|-------|------ |
| CyclicBarrier   | avgt |   5 | 2.202 ± | 0.686 | ms/op |
| ThreadPool      | avgt |   5 | 6.073 ± | 4.685 | ms/op |
| SingleThread    | avgt |   5 | 3.830 ± | 0.049 | ms/op |

##### With number of cells to process by task delay
| Benchmark       | Mode | Cnt | Score      | Error   | Units |
|-----------------|------|-----|------------|---------|-------|
| CyclicBarrier   | avgt |   5 |    6.751 ± |   0.316 | ms/op |
| ThreadPool      | avgt |   5 |  392.589 ± |   4.785 | ms/op |
| SingleThread    | avgt |   5 | 3370.461 ± | 204.707 | ms/op |
