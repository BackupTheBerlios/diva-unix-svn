package arf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.arf.ReasoningParameters;
import service.arf.reasoner.ReasonerHandle;

/**
 * The reasoning protocol class contains information about a reasoning and
 * validation task.
 */
public class Protocol implements service.arf.Protocol {

  /**
   * Reasoning parameters object for values of a reasoning and validation task.
   */
  protected ReasoningParameters                      parameters    = new ReasoningParameters();
  /**
   * List of all reasoner handles, which represents all used reasoner during
   * reasoning and validation.
   */
  protected List<ReasonerHandle>                     reasoner      = new ArrayList<ReasonerHandle>();
  /**
   * Map of reasoner handle and its reasoning parameters, which contains for a
   * used reasoner all values regarding a reasoning and validation task.
   */
  protected Map<ReasonerHandle, ReasoningParameters> parametersMap = new HashMap<ReasonerHandle, ReasoningParameters>();

  /**
   * Constructs a reasoning protocol.
   */
  public Protocol() {
    parameters.LOGGING = Boolean.TRUE;
  }

  public ReasoningParameters getParameters() {
    return this.parameters;
  }

  public ReasoningParameters getParameters(ReasonerHandle reasoner) {
    ReasoningParameters parameters = this.parametersMap.get(reasoner);
    return parameters;
  }

  public List<ReasonerHandle> getUsedReasoner() {
    return this.reasoner;
  }

  /**
   * Adds a used reasoner with its reasoning parameters values to this protocol.
   * 
   * @param reasoner
   *          The handle of the used reasoner.
   * @param parameters
   *          The reasoning parameters containing the values regarding the
   *          reasoning and validation task.
   */
  public void addUsedReasoner(ReasonerHandle reasoner, ReasoningParameters parameters) {
    if (this.reasoner.contains(reasoner) == false) {
      this.reasoner.add(reasoner);
      this.parametersMap.put(reasoner, parameters);
      if (parameters.MEMORY_USAGE != ReasoningParameters.UNKNOWN && parameters.MEMORY_USAGE != ReasoningParameters.DONT_CARE) {
        if (this.parameters.MEMORY_USAGE == ReasoningParameters.UNKNOWN || this.parameters.MEMORY_USAGE == ReasoningParameters.DONT_CARE) {
          this.parameters.MEMORY_USAGE = parameters.MEMORY_USAGE;
        }
        else {
          this.parameters.MEMORY_USAGE += parameters.MEMORY_USAGE;
        }
      }
      if (parameters.TIME_CONSUMPTION != ReasoningParameters.UNKNOWN && parameters.TIME_CONSUMPTION != ReasoningParameters.DONT_CARE) {
        if (this.parameters.TIME_CONSUMPTION == ReasoningParameters.UNKNOWN || this.parameters.TIME_CONSUMPTION == ReasoningParameters.DONT_CARE) {
          this.parameters.TIME_CONSUMPTION = parameters.TIME_CONSUMPTION;
        }
        else {
          this.parameters.TIME_CONSUMPTION += parameters.TIME_CONSUMPTION;
        }
      }
    }
  }
}
