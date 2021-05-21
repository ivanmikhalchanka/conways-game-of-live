package gameoflife.common;

import java.util.function.IntPredicate;

public class IntPredicates {

  private IntPredicates() {}

  public static IntPredicate lessThan(int bound) {
    return value -> value < bound;
  }

  public static IntPredicate isPositive() {
    return value -> value >= 0;
  }
}
