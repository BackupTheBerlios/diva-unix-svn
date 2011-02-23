package de.pure.diva.arf.rc.reasoner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RandomReasonerActivator implements BundleActivator {

  private RandomConfigurationReasoner creasoner = new RandomConfigurationReasoner();
  private RandomPathReasoner          preasoner = new RandomPathReasoner();

  public void start(BundleContext context) throws Exception {
    creasoner.start(context);
    preasoner.start(context);
  }

  public void stop(BundleContext context) throws Exception {
    creasoner.stop(context);
    preasoner.stop(context);
  }

}
