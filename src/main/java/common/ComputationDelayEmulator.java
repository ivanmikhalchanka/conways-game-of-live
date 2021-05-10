package common;

public class ComputationDelayEmulator {
  public static void emulateComputationDelay(int boardSize) {
    try {
      Thread.sleep(boardSize * 5L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
