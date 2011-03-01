package arf.question;

import java.util.HashMap;
import java.util.Map;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.Question;
import service.arf.question.QuestionHandle;
import service.arf.question.QuestionAttributes;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;

/**
 * The question trader manages all questions of the framework.
 */
public final class QuestionTrader {
  private static final class UNKNOWN implements Question {
    private static Question instance() {
      return new UNKNOWN();
    }

    public Result ask(ReasonerHandle reasoner, ModelHandle model, InputHandle input, QuestionHandle question) {
      return new UnknownResult();
    }

    public String getName() {
      return ARF.QUESTIONS.UNKNOWN;
    }

    public QuestionAttributes getAttributes() {
      return new QuestionAttributes();
    }

    public String getQuestion() {
      return "";
    }
  }

  private Map<String, Question> questions = new HashMap<String, Question>();
  private static QuestionTrader instance  = null;

  /**
   * Returns an instance of QuestionTrader class.
   * 
   * @return The singleton instance of this class.
   */
  public static QuestionTrader getInstance() {
    if (instance == null) {
      instance = new QuestionTrader();
    }
    return instance;
  }

  private QuestionTrader() {}

  /**
   * Returns a question for the given name.
   * 
   * @param name
   *          The name of a question.
   * @return The question. If no question was registered an UNKNOWN-question
   *         will be returned.
   */
  public Question getQuestion(String name) {
    Question question = this.questions.get(name);
    if (question == null) {
      question = UNKNOWN.instance();
    }
    return question;
  }

  /**
   * Called when a question is stared and available by the framework.
   * 
   * @param question
   *          The question.
   */
  public void registerQuestion(Question question) {
    this.questions.put(question.getName(), question);
  }

  /**
   * Called when a question is stopped and not available anymore by the
   * framework.
   * 
   * @param question
   *          The question.
   */
  public void unregisterQuestion(Question question) {
    this.questions.remove(question.getName());
  }
}
