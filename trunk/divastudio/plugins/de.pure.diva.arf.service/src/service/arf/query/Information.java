package service.arf.query;

import java.util.Collections;
import java.util.List;

import service.arf.reasoner.ReasonerHandle;

/**
 * The information interface provides the list of reasoner, which can answer a
 * question. A query returns such a information object.
 */
public interface Information {
  /**
   * An empty information object.
   */
  public final static class EMPTY implements Information {
    public static Information instance() {
      return new EMPTY();
    }

    public List<ReasonerHandle> getReasoner() {
      return Collections.emptyList();
    }
  }

  /**
   * Returns the list of reasoner.
   * 
   * @return All reasoner.
   */
  public List<ReasonerHandle> getReasoner();
}
