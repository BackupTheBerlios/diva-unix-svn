package factory;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.diva.factoryinstdiva.Factory;

/**
* This file was generated using DiVA Studio.
* Visit http://www.ict-diva.eu/ for more details about DiVA.
*/
public class Activator implements BundleActivator{

	@Override
	public void start(BundleContext context) throws Exception {
		Properties props = new Properties();
        props.put("Factory", "$BundleName$");
        context.registerService(
            Factory.class.getName(), factory.Factory.getFact(), props);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

}
