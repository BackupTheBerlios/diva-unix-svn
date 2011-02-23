package arf.question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;

import service.arf.question.Result;

/**
 * The resource result class represents the object which is returned by a
 * configuration question. The result contains a list of configuration URIs as
 * answer.
 */
public class URIResult extends ConfigurationResult implements Result {
  private List<String> answer = null;

  /**
   * Construct a default configuration result.
   * 
   * @param result
   *          The configuration result.
   */
  public URIResult(ConfigurationResult result) {
    if (result != null) {
      super.setAnswer(result.getAnswer());
      super.setLog(result.getLog());
    }
  }

  public List<String> getAnswerAsURI() {
    List<String> answer = this.answer;
    if (answer == null) {
      answer = new ArrayList<String>();
      List<Resource> l = super.getAnswer();
      for (Resource resource : l) {
        if (resource != null && resource.getURI() != null) {
          answer.add(resource.getURI().toString());
          save(resource);
        }
      }
    }
    return answer;
  }

  private void save(Resource resource) {
    // Save the contents of the resource to the file system.
    Map<Object, Object> options = new HashMap<Object, Object>();
    options.put(XMLResource.OPTION_ENCODING, "UTF-8");
    try {
      resource.save(options);
    }
    catch (IOException e) {}
  }

  @SuppressWarnings("unchecked")
  public void setAnswer(Object answer) {
    List<String> list = new ArrayList<String>();
    if (answer instanceof List) {
      for (Object object : ((List<?>) answer)) {
        if (object instanceof String) {
          list.add((String) object);
        }
      }
    }
    if (list.isEmpty() == false) {
      setAnswer(list);
    }
    else {
      super.setAnswer(answer);
    }
  }

  /**
   * Set the configuration answer of a question. This is done by a reasoner.
   * 
   * @param answer
   *          The answer.
   */
  public void setAnswerAsURI(List<String> answer) {
    this.answer = answer;
  }

}
