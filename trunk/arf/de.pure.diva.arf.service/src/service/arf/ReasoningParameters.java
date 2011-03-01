package service.arf;

import service.arf.question.QuestionAttributes;

/**
 * The reasoning parameters specifies a reasoning and validation task.
 */
public class ReasoningParameters extends QuestionAttributes {
  /**
   * Constant for an unknown value.
   */
  public final static Long UNKNOWN          = new Long(-1);
  /**
   * Constant for a value, whose parameter should be ignored.
   */
  public final static Long DONT_CARE        = UNKNOWN;

  /**
   * Logging enabling or disabling.
   */
  public Boolean           LOGGING          = Boolean.FALSE;
  /**
   * Value for maximal time consumption in seconds.
   */
  public Long              TIME_CONSUMPTION = DONT_CARE;
  /**
   * Value for maximal memory usage in Mbytes.
   */
  public Long              MEMORY_USAGE     = DONT_CARE;

  /**
   * Constructs new reasoning parameters with default values.
   */
  public ReasoningParameters() {}

  /**
   * Constructs new reasoning parameters from question attributes.
   * 
   * @param attributes
   *          The question attributes.
   */
  public ReasoningParameters(QuestionAttributes attributes) {
    OPTIMISING = attributes.OPTIMISING;
    SOLVING = attributes.SOLVING;
    VALIDATING = attributes.VALIDATING;
  }
}
