package gameoflife.factory.game;

import gameoflife.common.ThreadUtils;
import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.FutureGame;
import gameoflife.model.Board;
import gameoflife.renderer.BoardRenderer;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FixedThreadPoolFutureGameServiceFactory implements FutureGameServiceFactory {
  private final Board board;
  private final BoardRenderer renderer;
  private final ComputationDelayEmulator delayEmulator;

  @Override
  public FutureGame build() {
    int availableThreads = ThreadUtils.getNumberOfAvailableThreads();
    var executorService = Executors.newFixedThreadPool(availableThreads);

    return new FutureGame(board, renderer, delayEmulator, executorService, availableThreads);
  }
}
