package service.arf;

import java.util.Collections;
import java.util.List;

import service.arf.reasoner.ReasonerHandle;

/**
 * The protocol interface provides access to information about a reasoning and
 * validation task.
 */
public interface Protocol {
  /**
   * An empty protocol object.
   */
  public final static class EMPTY implements Protocol {
    public static Protocol instance() {
      return new EMPTY();
    }

    public ReasoningParameters getParameters() {
      return new ReasoningParameters();
    }

    public ReasoningParameters getParameters(ReasonerHandle reasoner) {
      return null;
    }

    public List<ReasonerHandle> getUsedReasoner() {
      return Collections.emptyList();
    }

    public void addUsedReasoner(ReasonerHandle reasoner, ReasoningParameters parameters) {}
  }

  /**
   * Get the parameters of a reasoning and validation task overall.
   * 
   * @return The parameters.
   */
  public ReasoningParameters getParameters();

  /**
   * Get the parameters of a reasoning and validation task for the given
   * reasoner.
   * 
   * @param reasoner
   *          A used reasoner.
   * @return The parameters.
   */
  public ReasoningParameters getParameters(ReasonerHandle reasoner);

  /**
   * Get the list of reasoner, which were used for reasoning and validation
   * task.
   * 
   * @return All used reasoner.
   */
  public List<ReasonerHandle> getUsedReasoner();

  /**
   * Adds a used reasoner with its reasoning parameters values to this protocol.
   * 
   * @param reasoner
   *          The handle of the used reasoner.
   * @param parameters
   *          The reasoning parameters containing the values regarding the
   *          reasoning and validation task.
   */
  public void addUsedReasoner(ReasonerHandle reasoner, ReasoningParameters parameters);
}
