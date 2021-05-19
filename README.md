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
