package arf;

public class SolverListener {
  private String    fault     = null;
  private Boolean   success   = null;
  private Semaphore semaphore = new Semaphore();

  public void setSuccess() {
    if (hasResult() == false) {
      this.success = true;
    }
    this.semaphore.release();
  }

  public void setFault(String fault) {
    if (hasResult() == false) {
      this.fault = fault;
      this.success = false;
    }
    this.semaphore.release();
  }

  public boolean hasSuccess() {
    return this.success != null && this.success == true;
  }

  public boolean hasFault() {
    return this.success != null && this.success == false;
  }

  public boolean hasResult() {
    return hasSuccess() == true || hasFault() == true;
  }

  public String getFaultMessage() {
    return this.fault == null || "".equals(this.fault) ? "Unknown application error." : this.fault;
  }

  private void synchronise() {
    this.semaphore.acquire();
  }

  public void synchronise(long timeout) throws Exception {
    synchronise("Solver:Timeouter", timeout);
  }

  protected void synchronise(String name, long timeout) throws Exception {
    TimeOuter timeouter = new TimeOuter(name, timeout, this);
    timeouter.start();
    synchronise();
    if (hasResult() == true) {
      timeouter.interrupt();
    }
    if (hasFault() == true) {
      throw new Exception(getFaultMessage());
    }
  }

  synchronized protected void finish() {
    if (hasResult() == false) {
      this.semaphore.release();
    }
  }

}
