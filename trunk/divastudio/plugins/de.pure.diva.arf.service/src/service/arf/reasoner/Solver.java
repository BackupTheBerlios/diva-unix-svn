package service.arf.reasoner;

import arf.question.ConfigurationResult;
import service.arf.InputHandle;
import service.arf.ModelHandle;

/**
 * The solver interface provides one of three tasks of a reasoner.
 */
public interface Solver {
  /**
   * Solve all configurations of the given reasoning model with the help of the
   * context and configuration model.
   * 
   * @param model
   *          The model handle containing the reasoning model.
   * @param input
   *          The input handle containing the context model and configuration
   *          model.
   * @return The result with all valid configurations.
   */
  public ConfigurationResult solve(ModelHandle model, InputHandle input);
}