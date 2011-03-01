package arf.question;

import service.arf.question.Question;

/**
 * This class is an abstract base class for questions of the adaptation
 * reasoning framework. This class registers and unregisters the question at the
 * QuestionTrader.
 */
public abstract class QuestionService extends AbstractQuestion implements Question {
  /**
   * Called when this question is started so the framework can perform the
   * question-specific activities necessary to start this question.
   */
  public void start() {
    QuestionTrader.getInstance().registerQuestion(this);
  }

  /**
   * Called when this question is stopped so the framework can perform the
   * question-specific activities necessary to stop the question.
   */
  public void stop() {
    QuestionTrader.getInstance().unregisterQuestion(this);
  }

}
