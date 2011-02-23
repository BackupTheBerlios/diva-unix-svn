package service.arf.reasoner;

import java.util.List;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;

/**
 * The reasoner interface provides all three tasks of a reasoner. Therefore it
 * extends the interfaces Optimiser, Solver and Validator.
 */
public interface Reasoner extends Optimiser, Solver, Validator {

  /**
   * Constant which defines that all questions are supported by a reasoner.
   */
  public static String[] ALL_QUESTIONS_SUPPORTED = { "Question" };
  /**
   * Constant which defines that all handles are supported by a reasoner.
   */
  public static String[] ALL_HANDLES_SUPPORTED   = { "ModelType", "InputType" };

  /**
   * Get the name of the reasoner.
   * 
   * @return The name.
   */
  public String getName();

  /**
   * Reason on the given input regarding the given question.
   * 
   * @param model
   *          The model handle containing the reasoning model.
   * @param input
   *          The input handle containing the context model and configuration
   *          model.
   * @param question
   *          The question handle containing the question and also the reasoning
   *          parameters.
   * @return The result.
   */
  public Result reason(ModelHandle model, InputHandle input, QuestionHandle question);

  /**
   * Get whether the reasoner provides an optimising task.
   * 
   * @return True, if optimising is provided, otherwise false.
   */
  public Boolean optimising();

  /**
   * Return whether the reasoner provides a solving task.
   * 
   * @return True, if solving is provided, otherwise false.
   */
  public Boolean solving();

  /**
   * Return whether the reasoner provides a validation task.
   * 
   * @return True, if validating is provided, otherwise false.
   */
  public Boolean validating();

  /**
   * Return all attributes, which rates the reasoner features.
   * 
   * @return The qualitative reasoning attributes.
   */
  public ReasonerAttributes getAttributes();

  /**
   * Get the supported questions of this reasoner.
   * 
   * @return All supported questions, which can be answered by this reasoner.
   */
  public List<String> getSupportedQuestions();

  /**
   * Get the supported types of model and input handles of this reasoner.
   * 
   * @return All supported types of model and input handles, which can be
   *         reasoned on by this reasoner.
   */
  public List<String> getSupportedHandles();
}
