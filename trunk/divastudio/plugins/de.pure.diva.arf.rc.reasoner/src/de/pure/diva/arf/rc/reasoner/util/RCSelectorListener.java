package de.pure.diva.arf.rc.reasoner.util;

import arf.SolverListener;
import arf.model.ARFConfiguration;

public class RCSelectorListener extends SolverListener {
  private static long TIMEOUT  = 2 * 60 * 1000;

  private RCSelector  selector = null;

  public RCSelectorListener(RCSelector selector) {
    this.selector = selector;
  }

  public void addConfiguration(ARFConfiguration configuration) {
    handleConfiguration(configuration);
  }

  protected void handleConfiguration(ARFConfiguration configuration) {}

  public void synchronise(long timeout) throws Exception {
    super.synchronise("RCSelector::Timeouter", timeout < 0 ? TIMEOUT : timeout);
  }

  synchronized protected void finish() {
    if (hasResult() == false) {
      this.selector.cancel(this);
    }
    super.finish();
  }

  public int count() {
    int count = this.selector.count();
    if (hasSuccess() == false) {
      count = 0;
    }
    return count;
  }

}
