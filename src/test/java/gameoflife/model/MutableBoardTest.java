package gameoflife.model;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MutableBoardTest {
  @ParameterizedTest
  @MethodSource("neighboursArguments")
  void testRetrieveNeighboursState(Cell cell, List<Cell> expectedNeighbours) {
    MutableBoard board = givenDefaultBoard();

    List<Cell> neighbours = board.getNeighbours(cell);

    assertEquals(expectedNeighbours, neighbours);
  }

  static Stream<Arguments> neighboursArguments() {
    Cell middleCell = new Cell(1, 1);
    List<Cell> middleNeighbours =
        range(0, 3)
            .boxed()
            .flatMap(row -> range(0, 3).mapToObj(col -> new Cell(row, col)))
            .filter(not(middleCell::equals))
            .collect(toList());

    return Stream.of(
        arguments(new Cell(0, 0), List.of(new Cell(0, 1), new Cell(1, 0), new Cell(1, 1))),
        arguments(middleCell, middleNeighbours),
        arguments(new Cell(2, 2), List.of(new Cell(1, 1), new Cell(1, 2), new Cell(2, 1))));
  }

  // ■ · ·
  // · · ■
  // ■ · ·
  private MutableBoard givenDefaultBoard() {
    MutableBoard board = new MutableBoard(3, 3);
    board.born(new Cell(0, 0));
    board.born(new Cell(1, 2));
    board.born(new Cell(2, 0));

    return board;
  }
}
