package arf.component;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.model.Loader;
import service.arf.query.Information;
import service.arf.query.ReasonerQueryHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import arf.question.URIResult;

/**
 * This file was generated using DiVA Studio. Visit http://www.ict-diva.eu/ for
 * more details about DiVA.
 */
public class ARF implements diva.reasoning.IReasoning, service.arf.ARF, eu.diva.osgi.component.DiVAComponentOSGi {

  /*
   * The following (generated) code deal with binding and unbinding the ports of
   * the component
   */

  private String instanceName;

  public String getInstanceName() {
    return instanceName;
  }

  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }

  private BundleContext context;

  public BundleContext getContext() {
    return context;
  }

  public void setContext(BundleContext context) {
    this.context = context;
  }

  /*
   * End of generated code. You can now implement the business logic of the
   * component (Quick Fix: Add Unimplemented Method)
   */
  private service.arf.ARF arf      = new arf.ARF();

  private static String   REASONER = "Alloy SAT Reasoner";

  public String getBestConfigurations(String model_uri, String context_uri) {
    String configuration_uri = "";
    try {
      arf.setReasoner(new arf.reasoner.ReasonerHandle(REASONER));
      Loader loader = new arf.model.util.Loader();
      ModelHandle model = new arf.ModelHandle(loader, model_uri);
      InputHandle input = new arf.InputHandle(loader, context_uri, null);
      QuestionHandle question = new arf.question.QuestionHandle(service.arf.ARF.QUESTIONS.GET_BEST_VALID_CONFIGURATIONS);
      arf.setModel(model);
      arf.setInput(input);
      Result result = ask(question);
      if (result instanceof URIResult) {
        List<String> answer = ((URIResult) result).getAnswerAsURI();
        if (answer.isEmpty() == false) {
          configuration_uri = answer.get(0);
        }
      }
    }
    finally {
      arf.setInput(null);
      arf.setModel(null);
      arf.setReasoner(null);
    }
    return configuration_uri;
  }

  public Result ask(QuestionHandle question) {
    return arf.ask(question);
  }

  public Information ask(ReasonerQueryHandle query, QuestionHandle question) {
    return arf.ask(query, question);
  }

  public ReasonerHandle loadReasoner(String bundleLocation) throws BundleException {
    return arf.loadReasoner(bundleLocation);
  }

  public void setInput(InputHandle input) {
    arf.setInput(input);
  }

  public void setModel(ModelHandle model) {
    arf.setModel(model);
  }

  public void setReasoner(ReasonerHandle reasoner) {
    arf.setReasoner(reasoner);
  }

  public void unloadReasoner(ReasonerHandle reasoner) throws BundleException {
    arf.unloadReasoner(reasoner);
  }

  public void start() {}

  public void stop() {}

}