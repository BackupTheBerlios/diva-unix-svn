package arf;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import arf.reasoner.ReasonerManager;

public class Activator implements BundleActivator {

  private static Activator activator = null;
  private BundleContext    context   = null;

  public void start(BundleContext context) throws Exception {
    Activator.activator = this;
    this.context = context;
    ARF.init();
  }

  public void stop(BundleContext context) throws Exception {
    stopBundleReasoner();
    this.context = null;
    Activator.activator = null;
  }

  private void stopBundleReasoner() {
    if (this.context != null) {
      Long[] bundleIDs = ReasonerManager.getInstance().getBundleIds();
      for (Long bundleID : bundleIDs) {
        if (bundleID != null) {
          Bundle bundle = context.getBundle(bundleID.longValue());
          if (bundle != null) {
            try {
              bundle.stop();
              bundle.uninstall();
            }
            catch (Exception e) {}
          }
        }
      }
    }
  }

  public static Activator getDefault() {
    Activator activator = Activator.activator;
    if (activator == null) {
      activator = new Activator();
    }
    return activator;
  }

  public BundleContext getContext() {
    return context;
  }
}
