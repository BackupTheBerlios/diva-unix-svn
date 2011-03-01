package service.arf.reasoner;

import arf.question.BooleanResult;
import service.arf.InputHandle;
import service.arf.ModelHandle;

/**
 * The validator interface provides one of three tasks of a reasoner.
 */
public interface Validator {
  /**
   * Validate the given configuration and context model regarding the reasoning
   * model.
   * 
   * @param model
   *          The model handle containing the reasoning model.
   * @param input
   *          The input handle containing the context model and configuration
   *          model.
   * @return The result as a Boolean value.
   */
  public BooleanResult validate(ModelHandle model, InputHandle input);
}