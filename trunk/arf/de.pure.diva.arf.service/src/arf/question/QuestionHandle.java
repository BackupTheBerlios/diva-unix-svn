package arf.question;

import service.arf.ARF;
import service.arf.ReasoningParameters;

/**
 * The question handle class encapsulates a question.
 */
public final class QuestionHandle implements service.arf.question.QuestionHandle {
  private String              question   = ARF.QUESTIONS.UNKNOWN;
  private ReasoningParameters parameters = null;

  /**
   * Constructs a question handle from a question.
   * 
   * @param question
   *          The question.
   */
  public QuestionHandle(String question) {
    this.question = question;
  }

  /**
   * Returns the name of the question.
   * 
   * @return The name of the appropriate question.
   */
  public String getQuestion() {
    return this.question;
  }

  /**
   * Returns the reasoning parameters which contains also the appropriate
   * question parameters.
   * 
   * @return The reasoning parameters.
   */
  public ReasoningParameters getParameters() {
    if (parameters == null) {
      parameters = new ReasoningParameters(QuestionTrader.getInstance().getQuestion(getQuestion()).getAttributes());
    }
    return parameters;
  }
}
