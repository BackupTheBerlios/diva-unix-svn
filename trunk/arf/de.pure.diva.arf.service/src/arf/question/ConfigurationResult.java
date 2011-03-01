package arf.question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.Log;
import service.arf.question.Result;

/**
 * The configuration result class represents the object which is returned by a
 * configuration question. The result contains a list of configurations as
 * answer.
 */
public class ConfigurationResult implements Result {

  private List<Resource> answer = Collections.emptyList();
  private Log            log    = Log.EMPTY.instance();

  /**
   * Construct a default configuration result.
   */
  public ConfigurationResult() {
    setAnswer(new ArrayList<Resource>());
    setLog(new arf.Log());
  }

  /**
   * Construct a configuration result.
   * 
   * @param answer
   *          The configuration answer.
   * @param log
   *          The logging information.
   */
  public ConfigurationResult(List<Resource> answer, Log log) {
    setAnswer(answer);
    setLog(log);
  }

  public List<Resource> getAnswer() {
    return this.answer;
  }

  public Log getLog() {
    return this.log;
  }

  @SuppressWarnings("unchecked")
  public void setAnswer(Object answer) {
    List<Resource> list = new ArrayList<Resource>();
    if (answer instanceof List) {
      for (Object object : ((List<?>) answer)) {
        if (object instanceof Resource) {
          list.add((Resource) object);
        }
      }
    }
    setAnswer(list);
  }

  /**
   * Set the configuration answer of a question. This is done by a reasoner.
   * 
   * @param answer
   *          The answer.
   */
  public void setAnswer(List<Resource> answer) {
    this.answer = answer;
  }

  public void setLog(Log log) {
    this.log = log;
  }

}
