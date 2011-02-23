package service.arf.question;

import service.arf.ReasoningParameters;

/**
 * The question handle encapsulates a question.
 */
public interface QuestionHandle {

  /**
   * Get the name of the question.
   * 
   * @return The name of the appropriate question.
   */
  public String getQuestion();

  /**
   * Get the reasoning parameters which contains also the appropriate question
   * attributes.
   * 
   * @return The reasoning parameters.
   */
  public ReasoningParameters getParameters();
}
