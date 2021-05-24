package gameoflife.game;

import gameoflife.common.ThreadUtils;
import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.ActivatedCell;
import gameoflife.game.state.BoardPartStateService;
import gameoflife.model.Board;
import gameoflife.renderer.BoardRenderer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompletableFutureGame extends CachingBoardGame {
  private final List<BoardPartStateService> partStateServices;

  public static void start(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    new CompletableFutureGame(board, renderer, delayEmulator).start();
  }

  private CompletableFutureGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    super(board, renderer, delayEmulator);

    partStateServices =
        super.buildBoardPartStateServices(ThreadUtils.getNumberOfAvailableThreads());
  }

  @Override
  public void start() {
    super.applyChangesUtilConverged(this::calculateChanges);
  }

  Stream<ActivatedCell> calculateChanges() {
    List<CompletableFuture<Stream<ActivatedCell>>> futures =
        partStateServices.stream()
            .map(service -> CompletableFuture.supplyAsync(() -> service.calculateNextState(board)))
            .collect(Collectors.toList());

    // Perform join in a separate Stream in order to prevent it's invocation before all tasks
    // submitted
    return futures.stream().flatMap(CompletableFuture::join);
  }
}
