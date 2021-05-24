package gameoflife.emulator;

public class ThreadSleepComputationDelayEmulator implements ComputationDelayEmulator {
  private final Long delayMultiplier;

  public ThreadSleepComputationDelayEmulator(Long delayMultiplier) {
    this.delayMultiplier = delayMultiplier;
  }

  @Override
  public void emulateComputationDelay(int numOfItems) {
    try {
      Thread.sleep(numOfItems * delayMultiplier);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }
}
