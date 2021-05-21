package gameoflife.common;

public class ThreadUtils {

  private ThreadUtils() {}

  public static int getNumberOfAvailableThreads() {
    int availableProcessors = Runtime.getRuntime().availableProcessors();

    // 1 - for main thread
    return availableProcessors - 1;
  }
}
