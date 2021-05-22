package gameoflife.game;

import static java.util.stream.IntStream.range;

import gameoflife.emulator.ComputationDelayEmulator;
import gameoflife.game.state.ActivatedCell;
import gameoflife.game.state.BoardPartStateService;
import gameoflife.game.state.CachingBoard;
import gameoflife.model.Board;
import gameoflife.renderer.BoardRenderer;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CachingBoardGame implements Game {
  protected final CachingBoard board;
  protected final BoardRenderer renderer;
  protected final ComputationDelayEmulator delayEmulator;

  protected CachingBoardGame(
      Board board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    this.board = new CachingBoard(board);
    this.renderer = renderer;
    this.delayEmulator = delayEmulator;
  }

  protected void applyChangesUtilConverged(Supplier<Stream<ActivatedCell>> changes) {
    while (!board.hasConverged()) {
      board.commitChanges();
      renderer.render(board);

      BoardPartStateService.applyChanges(changes.get(), board);
    }
  }

  protected List<BoardPartStateService> buildBoardPartStateServices(int amount) {
    return range(0, amount)
        .mapToObj(index -> this.board.getPartOfCells(amount, index))
        .map(cells -> new BoardPartStateService(cells, delayEmulator))
        .collect(Collectors.toList());
  }
}
