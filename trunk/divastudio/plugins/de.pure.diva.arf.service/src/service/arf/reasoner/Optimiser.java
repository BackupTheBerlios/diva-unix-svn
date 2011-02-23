package service.arf.reasoner;

import arf.question.ConfigurationResult;
import service.arf.InputHandle;
import service.arf.ModelHandle;

/**
 * The optimiser interface provides one of three tasks of a reasoner.
 */
public interface Optimiser {
  /**
   * Optimise one configuration of the given reasoning model with the help of
   * the context and configuration model.
   * 
   * @param model
   *          The model handle containing the reasoning model.
   * @param input
   *          The input handle containing the context model and configuration
   *          model.
   * @return The result with any valid configuration.
   */
  public ConfigurationResult optimise(ModelHandle model, InputHandle input);
}
