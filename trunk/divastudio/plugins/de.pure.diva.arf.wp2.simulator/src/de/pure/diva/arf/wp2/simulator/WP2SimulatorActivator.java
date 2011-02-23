package de.pure.diva.arf.wp2.simulator;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import arf.question.QuestionService;
import de.pure.diva.arf.wp2.simulator.question.ComputeConfigScoresQuestion;
import de.pure.diva.arf.wp2.simulator.question.ComputeConfigVerdictsQuestion;
import de.pure.diva.arf.wp2.simulator.question.ComputePropertyPrioritiesQuestion;
import de.pure.diva.arf.wp2.simulator.question.GetAllSuitableConfigsQuestion;
import de.pure.diva.arf.wp2.simulator.question.PerformCompleteSimulationQuestion;

/**
 * The activator class controls the plug-in life cycle
 */
public class WP2SimulatorActivator extends AbstractUIPlugin {

  private final static class QUESTIONS {
    private final static QuestionService PERFORM_COMPLETE_SIMULATION     = new PerformCompleteSimulationQuestion();
    private final static QuestionService GET_ALL_SUITABLE_CONFIGURATIONS = new GetAllSuitableConfigsQuestion();
    private final static QuestionService COMPUTE_PROPERTY_PRIORITIES     = new ComputePropertyPrioritiesQuestion();
    private final static QuestionService COMPUTE_CONFIGURATION_SCORES    = new ComputeConfigScoresQuestion();
    private final static QuestionService COMPUTE_CONFIGURATION_VERDICTS  = new ComputeConfigVerdictsQuestion();
  }

  // The plug-in ID
  public static final String           PLUGIN_ID = "de.pure.diva.arf.wp2.simulator";

  // The shared instance
  private static WP2SimulatorActivator plugin;

  /**
   * The constructor
   */
  public WP2SimulatorActivator() {}

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
   * )
   */
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
    QUESTIONS.PERFORM_COMPLETE_SIMULATION.start();
    QUESTIONS.GET_ALL_SUITABLE_CONFIGURATIONS.start();
    QUESTIONS.COMPUTE_PROPERTY_PRIORITIES.start();
    QUESTIONS.COMPUTE_CONFIGURATION_SCORES.start();
    QUESTIONS.COMPUTE_CONFIGURATION_VERDICTS.start();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
   * )
   */
  public void stop(BundleContext context) throws Exception {
    QUESTIONS.PERFORM_COMPLETE_SIMULATION.stop();
    QUESTIONS.GET_ALL_SUITABLE_CONFIGURATIONS.stop();
    QUESTIONS.COMPUTE_PROPERTY_PRIORITIES.stop();
    QUESTIONS.COMPUTE_CONFIGURATION_SCORES.stop();
    QUESTIONS.COMPUTE_CONFIGURATION_VERDICTS.stop();
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static WP2SimulatorActivator getDefault() {
    return plugin;
  }

}
