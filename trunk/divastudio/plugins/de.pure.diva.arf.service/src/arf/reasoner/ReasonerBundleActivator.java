package arf.reasoner;

import org.osgi.framework.BundleActivator;

import service.arf.reasoner.Reasoner;

/**
 * This class is an abstract class which can be used for reasoner, which are
 * implemented in a bundle and are also an activator. This class registers the
 * reasoner as service with all supported questions, and model classes.
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
public abstract class ReasonerBundleActivator extends ReasonerService implements Reasoner, BundleActivator {}
