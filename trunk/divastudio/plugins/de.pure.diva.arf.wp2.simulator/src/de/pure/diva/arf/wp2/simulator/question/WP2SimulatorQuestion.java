package de.pure.diva.arf.wp2.simulator.question;

import arf.question.QuestionService;

public abstract class WP2SimulatorQuestion extends QuestionService {
  /**
   * Constants for all names of questions which are fixed part of the framework.
   */
  public final static class QUESTIONS {
    public final static String PERFORM_COMPLETE_SIMULATION     = "PERFORM_COMPLETE_SIMULATION";
    public final static String GET_ALL_SUITABLE_CONFIGURATIONS = "GET_ALL_SUITABLE_CONFIGURATIONS";
    public final static String COMPUTE_PROPERTY_PRIORITIES     = "COMPUTE_PROPERTY_PRIORITIES";
    public final static String COMPUTE_CONFIGURATION_SCORES    = "COMPUTE_CONFIGURATION_SCORES";
    public final static String COMPUTE_CONFIGURATION_VERDICTS  = "COMPUTE_CONFIGURATION_VERDICTS";
  }

}
