package arf.question;

import service.arf.Log;
import service.arf.question.Result;

/**
 * The numeric result class represents the object which is returned by a numeric
 * question. The result contains a numeric answer.
 */
public class NumericResult implements Result {
  private Integer answer = Integer.MIN_VALUE;
  private Log     log    = Log.EMPTY.instance();

  /**
   * Construct a default numeric result.
   */
  public NumericResult() {
    setAnswer(0);
    setLog(new arf.Log());
  }

  /**
   * Construct a numeric result.
   * 
   * @param answer
   *          The numeric answer.
   * @param log
   *          The logging information.
   */
  public NumericResult(Integer answer, Log log) {
    setAnswer(answer);
    setLog(log);
  }

  public Integer getAnswer() {
    return this.answer;
  }

  public Log getLog() {
    return this.log;
  }

  public void setAnswer(Object answer) {
    if (answer instanceof Integer) {
      setAnswer((Integer) answer);
    }
  }

  /**
   * Set the numeric answer of a question. This is done by a reasoner.
   * 
   * @param answer
   *          The answer.
   */
  public void setAnswer(Integer answer) {
    this.answer = answer;
  }

  public void setLog(Log log) {
    this.log = log;
  }
}
