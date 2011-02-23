package arf.reasoner;

import service.arf.reasoner.Reasoner;
import service.arf.reasoner.ReasonerAttributes;

/**
 * The reasoner handle class encapsulates a reasoner.
 */
public class ReasonerHandle implements service.arf.reasoner.ReasonerHandle {

  private String             reasoner   = null;
  private ReasonerAttributes parameters = null;

  /**
   * Constructs a reasoner handle from a reasoner.
   * 
   * @param reasoner
   *          The reasoner.
   */
  public ReasonerHandle(String reasoner) {
    this.reasoner = reasoner;
  }

  /**
   * Returns the name of the reasoner.
   * 
   * @return The name.
   */
  public String getReasoner() {
    return this.reasoner;
  }

  /**
   * Returns the parameter of the appropriate reasoner.
   * 
   * @return The reasoner parameter.
   */
  public ReasonerAttributes getAttributes() {
    if (this.parameters == null) {
      Reasoner reasoner = ReasonerManager.getInstance().getReasoner(this.getReasoner());
      ReasonerAttributes parameters = reasoner.getAttributes();
      this.parameters = new ReasonerAttributes(parameters);
    }
    return this.parameters;
  }
}
