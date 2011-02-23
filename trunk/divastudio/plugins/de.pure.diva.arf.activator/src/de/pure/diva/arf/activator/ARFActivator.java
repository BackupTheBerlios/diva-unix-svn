package de.pure.diva.arf.activator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import service.arf.reasoner.Reasoner;
import arf.reasoner.ReasonerBundleActivator;
import arf.reasoner.ReasonerManager;
import arf.reasoner.ReasonerService;

/**
 * The activator class controls the plug-in life cycle
 */
public class ARFActivator extends Plugin {

  // The reasoner extension ID
  private static String       EXTENSION_REASONER = "reasoner";

  // The plug-in ID
  public static final String  PLUGIN_ID          = "de.pure.diva.arf.activator";

  // The shared instance
  private static ARFActivator plugin;

  /**
   * The constructor
   */
  public ARFActivator() {}

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
    loadReasoner();
  }

  private void loadReasoner() {
    if (plugin != null && plugin.getBundle() != null) {
      List<IConfigurationElement> elements = new ArrayList<IConfigurationElement>();
      String pid = plugin.getBundle().getSymbolicName();
      IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(pid, EXTENSION_REASONER);
      if (point != null) {
        IExtension[] extensions = point.getExtensions();
        for (IExtension extension : extensions) {
          elements.addAll(Arrays.asList(extension.getConfigurationElements()));
        }
      }
      for (IConfigurationElement element : elements) {
        try {
          Object o = element.createExecutableExtension("class");
          if (o instanceof Reasoner) {
            Reasoner reasoner = (Reasoner) o;
            Bundle bundle = Platform.getBundle(element.getNamespaceIdentifier());
            BundleContext context = bundle != null ? bundle.getBundleContext() : null;
            if (o instanceof ReasonerBundleActivator == false) {
              if (o instanceof ReasonerService) {
                ReasonerService service = (ReasonerService) o;
                if (service instanceof BundleActivator == false) {
                  service.start(context);
                }
              }
              else {
                ReasonerManager.getInstance().registerReasoner(context, reasoner);
              }
            }
            else {
              // The reasoner is implemented as a bundle activator.
              // Thus it registers itself to the reasoner manager.
            }
          }
        }
        catch (Exception e) {
          String msg = e.getMessage() == null ? "Unknown application error: " + e.getClass() : e.getMessage();
          ARFActivator.getDefault().getLog().log(new Status(Status.ERROR, ARFActivator.PLUGIN_ID, msg, e));
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static ARFActivator getDefault() {
    return plugin;
  }

}
