package service.arf.question;

import service.arf.Log;

/**
 * The result interface represents the object which is returned by a question.
 * The result object contains a question dependent answer.
 */
public interface Result {
  /**
   * An empty logging information object.
   */
  public final static class EMPTY implements Result {
    public static Result instance() {
      return new EMPTY();
    }

    public Object getAnswer() {
      return new Object();
    }

    public Log getLog() {
      return Log.EMPTY.instance();
    }

    public void setAnswer(Object answer) {}

    public void setLog(Log log) {}
  }

  /**
   * Set the answer of a question. This is done by a reasoner.
   * 
   * @param answer
   *          The answer.
   */
  public void setAnswer(Object answer);

  /**
   * Get the answer of a question.
   * 
   * @return The answer.
   */
  public Object getAnswer();

  /**
   * Set the logging object of the reasoning and validation task.
   * 
   * @param log
   *          The logging object.
   */
  public void setLog(Log log);

  /**
   * Get the logging object of the reasoning and validation task.
   * 
   * @return The logging object.
   */
  public Log getLog();
}
