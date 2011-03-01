package arf.reasoner;

import org.osgi.framework.BundleContext;

import service.arf.reasoner.Reasoner;

/**
 * This class is an abstract class which can be used for reasoner, which are
 * implemented in a bundle. This class registers the reasoner as service with
 * all supported questions, and model classes.
 * 
 * <p>
 * The service is registered for the following properties:
 * <ol>
 * <li>ReasonerManager.DICTIONARY_ENTRY_REASONER->Reasoner.getName()</li>
 * <li>Question.getName()->Reasoner.getName()</li>
 * <li>ModelHandle.class.getName()->Reasoner.getName()</li>
 * <li>InputHandle.class.getName()->Reasoner.getName()</li>
 * </ol>
 */
public abstract class ReasonerService implements Reasoner {

  /**
   * Called when this reasoner is started so the framework can perform the
   * reasoner-specific activities necessary to start this reasoner. This method
   * is called from a bundle activator and can be used to register services or
   * to allocate any resources that this reasoner needs.
   * 
   * <p>
   * This method must complete and return to its caller in a timely manner.
   * 
   * @param context
   *          The execution context of the bundle being started.
   */
  public void start(BundleContext context) throws Exception {
    ReasonerManager.getInstance().registerReasoner(context, this);
  }

  /**
   * Called when this reasoner is stopped so the framework can perform the
   * reasoner-specific activities necessary to stop the reasoner. In general,
   * this method should undo the work that the
   * <code>ReasonerActivator.start</code> method started. There should be no
   * active threads that were started by this reasoner when this reasoner
   * returns. A stopped reasoner must not call any framework objects. This
   * method is called from a bundle activator.
   * 
   * <p>
   * This method must complete and return to its caller in a timely manner.
   * 
   * @param context
   *          The execution context of the bundle being stopped.
   */
  public void stop(BundleContext context) throws Exception {
    ReasonerManager.getInstance().unregisterReasoner(this);
  }

}
