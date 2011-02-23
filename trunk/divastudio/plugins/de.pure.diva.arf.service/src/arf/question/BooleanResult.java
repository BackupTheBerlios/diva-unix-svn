package arf.question;

import service.arf.Log;
import service.arf.question.Result;

/**
 * The Boolean result class represents the object which is returned by a Boolean
 * question. The result contains a Boolean answer.
 */
public class BooleanResult implements Result {

  private Boolean answer = null;
  private Log     log    = Log.EMPTY.instance();

  /**
   * Construct a default Boolean result.
   */
  public BooleanResult() {
    setAnswer(Boolean.FALSE);
    setLog(new arf.Log());
  }

  /**
   * Construct a Boolean result.
   * 
   * @param answer
   *          The Boolean answer.
   * @param log
   *          The logging information.
   */
  public BooleanResult(Boolean answer, Log log) {
    setAnswer(answer);
    setLog(log);
  }

  public Boolean getAnswer() {
    return this.answer;
  }

  public Log getLog() {
    return this.log;
  }

  public void setAnswer(Object answer) {
    if (answer instanceof Boolean) {
      setAnswer((Boolean) answer);
    }
  }

  /**
   * Set the Boolean answer of a question. This is done by a reasoner.
   * 
   * @param answer
   *          The answer.
   */
  public void setAnswer(Boolean answer) {
    this.answer = answer;
  }

  public void setLog(Log log) {
    this.log = log;
  }
}
