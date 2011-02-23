package service.arf.reasoner;

/**
 * The reasoner handle encapsulates a reasoner.
 */
public interface ReasonerHandle {
  /**
   * Get the name of the reasoner.
   * 
   * @return The name.
   */
  public String getReasoner();

  /**
   * Get the attributes of the appropriate reasoner.
   * 
   * @return The reasoner attributes.
   */
  public ReasonerAttributes getAttributes();
}
