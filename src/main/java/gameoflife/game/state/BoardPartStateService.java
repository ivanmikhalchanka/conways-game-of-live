package gameoflife.game.state;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

import java.util.stream.Stream;
import gameoflife.emulator.ComputationDelayEmulator;
import java.util.List;
import java.util.Map;
import gameoflife.model.Board;
import gameoflife.model.Cell;

public class BoardPartStateService {
  private final List<Cell> cellsToProcess;
  private final ComputationDelayEmulator delayEmulator;
  private final BoardStateService boardStateService;

  public static void applyChanges(Stream<ActivatedCell> changes, Board board) {
    Map<Boolean, List<Cell>> cellsByActiveFlag =
        changes.collect(
            partitioningBy(ActivatedCell::isActive, mapping(ActivatedCell::getCell, toList())));

    cellsByActiveFlag.getOrDefault(true, emptyList()).forEach(board::born);
    cellsByActiveFlag.getOrDefault(false, emptyList()).forEach(board::kill);
  }

  public BoardPartStateService(List<Cell> cellsToProcess, ComputationDelayEmulator delayEmulator) {
    this.cellsToProcess = cellsToProcess;
    this.delayEmulator = delayEmulator;

    boardStateService = new BoardStateService(numOfItems -> {});
  }

  public void moveToNextState(Board board) {
    Stream<ActivatedCell> changes = calculateNextState(board);

    applyChanges(changes, board);
  }

  public Stream<ActivatedCell> calculateNextState(Board board) {
    delayEmulator.emulateComputationDelay(cellsToProcess.size());

    return cellsToProcess.stream()
        .map(cell -> new ActivatedCell(cell, boardStateService.shouldBeAlive(cell, board)));
  }
}
