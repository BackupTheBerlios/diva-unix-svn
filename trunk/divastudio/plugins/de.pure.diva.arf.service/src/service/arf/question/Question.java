package service.arf.question;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.reasoner.ReasonerHandle;

/**
 * A question interface specifies what to do by the framework.
 */
public interface Question {

  /**
   * Get the name of the question.
   * 
   * @return The name.
   */
  public String getName();

  /**
   * Get the question.
   * 
   * @return The question.
   */
  public String getQuestion();

  /**
   * get the question attributes, which characterise the question.
   * 
   * @return The question attributes.
   */
  public QuestionAttributes getAttributes();

  /**
   * Called when the framework is asked for reasoning and validation.
   * 
   * @param reasoner
   *          The reasoner handle.
   * @param model
   *          The work model handle.
   * @param input
   *          The work input handle.
   * @param question
   *          The question handle.
   * @return The result object of the reasoner.
   */
  public Result ask(ReasonerHandle reasoner, ModelHandle model, InputHandle input, QuestionHandle question);
}
