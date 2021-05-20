package service.emulator;

public class ThreadSleepComputationDelayEmulator implements ComputationDelayEmulator {
  private final Long delayMultiplier;

  public ThreadSleepComputationDelayEmulator(Long delayMultiplier) {
    this.delayMultiplier = delayMultiplier;
  }

  @Override
  public void emulateComputationDelay(int boardSize) {
    try {
      Thread.sleep(boardSize * delayMultiplier);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
