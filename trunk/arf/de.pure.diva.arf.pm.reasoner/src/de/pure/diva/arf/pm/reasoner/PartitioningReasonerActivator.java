package de.pure.diva.arf.pm.reasoner;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PartitioningReasonerActivator implements BundleActivator {

  private PartitioningReasoner reasoner = new PartitioningReasoner();

  public void start(BundleContext context) throws Exception {
    reasoner.start(context);
  }

  public void stop(BundleContext context) throws Exception {
    reasoner.stop(context);
  }

}
