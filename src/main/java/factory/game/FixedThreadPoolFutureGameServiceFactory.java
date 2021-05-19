package factory.game;

import common.ThreadUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import model.CachingBoard;
import renderer.BoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.game.FutureGameService;

@RequiredArgsConstructor
public class FixedThreadPoolFutureGameServiceFactory implements FutureGameServiceFactory {
  private final CachingBoard board;
  private final BoardRenderer renderer;
  private final ComputationDelayEmulator delayEmulator;

  @Override
  public FutureGameService build() {
    int availableThreads = ThreadUtils.getNumberOfAvailableThreads();
    ExecutorService executorService = Executors.newFixedThreadPool(availableThreads);

    return new FutureGameService(board, renderer, delayEmulator, executorService, availableThreads);
  }
}
