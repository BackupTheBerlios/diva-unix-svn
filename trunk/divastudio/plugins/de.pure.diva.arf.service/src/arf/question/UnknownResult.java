package arf.question;

import service.arf.Log;
import service.arf.question.Result;

/**
 * The unknown result class represents the object which is returned if no
 * reasoner is available or no question was found for a handle. The result
 * contains an object answer.
 */
public class UnknownResult implements Result {
  private Object answer = null;
  private Log    log    = Log.EMPTY.instance();

  public UnknownResult() {
    setAnswer(new Object());
    setLog(new arf.Log());
  }

  public Object getAnswer() {
    return this.answer;
  }

  public Log getLog() {
    return log;
  }

  public void setAnswer(Object answer) {
    this.answer = answer;
  }

  public void setLog(Log log) {
    this.log = log;
  }
}
