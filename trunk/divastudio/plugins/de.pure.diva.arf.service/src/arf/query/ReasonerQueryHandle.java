package arf.query;


/**
 * The reasoner query handle class encapsulates a reasoner query.
 */
public class ReasonerQueryHandle implements service.arf.query.ReasonerQueryHandle {
  private String query = service.arf.ARF.QUERIES.UNKNOWN;

  /**
   * Constructs a reasoner query handle from a reasoner query.
   * 
   * @param query
   *          The reasoner query.
   */
  public ReasonerQueryHandle(String query) {
    this.query = query;
  }

  /**
   * Returns the name of the reasoner query.
   * 
   * @return The name of the appropriate query.
   */
  public String getQuery() {
    return this.query;
  }
}
