package factory.arf;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.diva.factoryinstdiva.Factory;

/**
 * This file was generated using DiVA Studio. Visit http://www.ict-diva.eu/ for
 * more details about DiVA.
 */
public class Activator implements BundleActivator {

  public static BundleContext context;

  public void start(BundleContext context) throws Exception {
    this.context = context;
    Properties props = new Properties();
    props.put("Factory", "de.pure.diva.arf.component");
    context.registerService(Factory.class.getName(), factory.arf.Factory.getFact(), props);
  }

  public void stop(BundleContext context) throws Exception {}

}