package arf.question;

import service.arf.Log;
import service.arf.question.Result;

/**
 * The string result class represents the object which is returned by a string
 * question. The result contains a string answer.
 */
public class StringResult implements Result {

  private String answer = null;
  private Log    log    = Log.EMPTY.instance();

  /**
   * Construct a default string result.
   */
  public StringResult() {
    setAnswer("");
    setLog(new arf.Log());
  }

  /**
   * Construct a string result.
   * 
   * @param answer
   *          The string answer.
   * @param log
   *          The logging information.
   */
  public StringResult(String answer, Log log) {
    setAnswer(answer);
    setLog(log);
  }

  public String getAnswer() {
    return this.answer;
  }

  public Log getLog() {
    return this.log;
  }

  public void setAnswer(Object answer) {
    if (answer instanceof String) {
      setAnswer((Boolean) answer);
    }
  }

  /**
   * Set the string answer of a question. This is done by a reasoner.
   * 
   * @param answer
   *          The answer.
   */
  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public void setLog(Log log) {
    this.log = log;
  }

}
