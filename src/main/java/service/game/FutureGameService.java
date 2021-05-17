package service.game;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import model.ActivatedCell;
import model.CachingBoard;
import model.Cell;
import renderer.BoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.state.BoardPartStateService;

public class FutureGameService implements GameService {
  private final CachingBoard board;
  private final BoardRenderer renderer;
  private final List<BoardPartStateService> partStateServices;

  public FutureGameService(
      CachingBoard board, BoardRenderer renderer, ComputationDelayEmulator delayEmulator) {
    this.board = board;
    this.renderer = renderer;

    int availableProcessors = Runtime.getRuntime().availableProcessors();

    partStateServices =
        range(0, availableProcessors)
            .mapToObj(index -> this.board.getPartOfCells(availableProcessors, index))
            .map(cells -> new BoardPartStateService(cells, delayEmulator))
            .collect(toList());
  }

  @Override
  public void start() {
    ExecutorService threadPool = Executors.newFixedThreadPool(partStateServices.size());
    ExecutorCompletionService<Stream<ActivatedCell>> completionService =
        new ExecutorCompletionService<>(threadPool);

    while (!board.hasConverged()) {
      board.commitChanges();
      renderer.render(board);

      partStateServices.stream()
          .map(service -> (Callable<Stream<ActivatedCell>>) () -> service.calculateNextState(board))
          .forEach(completionService::submit);

      moveToNextState(completionService);
    }
  }

  private void moveToNextState(ExecutorCompletionService<Stream<ActivatedCell>> completionService) {
    int completed = 0;
    while (completed++ < partStateServices.size()) {
      Map<Boolean, List<Cell>> cellsByActiveFlag =
          retrieveNext(completionService)
              .collect(
                  groupingBy(ActivatedCell::isActive, mapping(ActivatedCell::getCell, toList())));

      cellsByActiveFlag.getOrDefault(true, emptyList()).forEach(board::born);
      cellsByActiveFlag.getOrDefault(false, emptyList()).forEach(board::kill);
    }
  }

  Stream<ActivatedCell> retrieveNext(
      ExecutorCompletionService<Stream<ActivatedCell>> completionService) {
    try {
      Future<Stream<ActivatedCell>> future = completionService.take();

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
