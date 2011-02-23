package service.arf.reasoner;

/**
 * The reasoner attributes rates reasoner feature with qualitative values.
 */
public class ReasonerAttributes {
  /**
   * Constant for an unknown value.
   */
  public final static Integer UNKNOWN      = 0;
  /**
   * Constant for slow reasoning and validation.
   */
  public final static Integer SLOW         = 1;
  /**
   * Constant for low memory usage during reasoning and validation.
   */
  public final static Integer LOW          = 1;
  /**
   * Constant for medium reasoning and validation and also for medium memory
   * usage during reasoning and validation.
   */
  public final static Integer MEDIUM       = 5;
  /**
   * Constant for fast reasoning and validation.
   */
  public final static Integer FAST         = 9;
  /**
   * Constant for high memory usage during reasoning and validation.
   */
  public final static Integer HIGH         = 9;
  /**
   * Constant for optimal reasoning and validation.
   */
  public final static Integer OPTIMAL      = 100;
  /**
   * Constant for none optimal reasoning and validation.
   */
  public final static Integer NONE_OPTIMAL = -100;

  /**
   * Rating for time consumption.
   */
  public Integer              TIME         = UNKNOWN;
  /**
   * Rating for memory usage.
   */
  public Integer              MEMORY       = UNKNOWN;
  /**
   * Rating for reasoning.
   */
  public Integer              REASONING    = UNKNOWN;

  /**
   * Constructs new reasoner attributes with default ratings.
   */
  public ReasonerAttributes() {}

  /**
   * Constructs new reasoner attributes from reasoner attributes.
   * 
   * @param attributes
   *          The reasoner attributes.
   */
  public ReasonerAttributes(ReasonerAttributes attributes) {
    if (attributes != null) {
      this.TIME = attributes.TIME;
      this.MEMORY = attributes.MEMORY;
      this.REASONING = attributes.REASONING;
    }
  }
}
