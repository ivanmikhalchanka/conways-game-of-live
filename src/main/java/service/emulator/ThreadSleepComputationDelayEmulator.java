package service.emulator;

public class ThreadSleepComputationDelayEmulator implements ComputationDelayEmulator {
  public void emulateComputationDelay(int boardSize) {
    try {
      Thread.sleep(boardSize * 5L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
