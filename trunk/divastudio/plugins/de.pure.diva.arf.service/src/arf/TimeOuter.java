package arf;

import service.arf.ReasoningParameters;

public class TimeOuter extends Thread {
  private static long    span        = 20;
  private long           timeout     = ReasoningParameters.DONT_CARE;
  private SolverListener listener    = null;
  private boolean        interrupted = false;

  public TimeOuter(String name, long timeout, SolverListener listener) {
    this.timeout = timeout;
    this.listener = listener;
    super.setName(name);
  }

  @Override
  public void run() {
    if (this.timeout >= 0) {
      long time = System.currentTimeMillis();
      long deadline = time + this.timeout;
      while (isInterrupted() == false && time < deadline) {
        try {
          sleep(span);
        }
        catch (InterruptedException e) {}
        time = System.currentTimeMillis();
      }
      this.listener.finish();
    }
  }

  @Override
  public void interrupt() {
    this.interrupted = true;
    super.interrupt();
  }

  @Override
  public boolean isInterrupted() {
    return this.interrupted == true || super.isInterrupted();
  }
}
