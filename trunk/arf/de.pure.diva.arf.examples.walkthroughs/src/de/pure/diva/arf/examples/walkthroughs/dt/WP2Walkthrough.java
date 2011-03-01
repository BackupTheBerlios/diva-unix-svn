package de.pure.diva.arf.examples.walkthroughs.dt;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.InputHandle;
import service.arf.Log;
import service.arf.ModelHandle;
import service.arf.Protocol;
import service.arf.Statistics;
import service.arf.query.Information;
import service.arf.query.ReasonerQueryHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import service.arf.reasoner.ReasonerAttributes;
import arf.ARF;
import arf.question.ConfigurationResult;
import de.pure.diva.arf.examples.walkthroughs.Walkthrough;

/**
 * The WP2 walkthrough as described in the Deliverable D4.2.
 */
public class WP2Walkthrough extends Walkthrough {

  /**
   * Dummy method for getting the WP2 reasoning model.
   * 
   * @return The DiVA variability model which is <b>null</b>.
   */
  private Resource getVariabilityModel() {
    return null;
  }

  @Override
  public void run() {
    // getting variability model
    Resource vm = getVariabilityModel();
    // creation of model handle with adaptation model
    ModelHandle mhandle = new arf.ModelHandle(vm);
    // getting context model
    Resource context = null;
    // creation of level-dependent input handle with context
    InputHandle ihandle = new arf.InputHandle(context, null);
    // creation of a handle for GET_ALL_VALID_CONFIGURATIONS-question
    QuestionHandle qhandle = new arf.question.QuestionHandle(service.arf.ARF.QUESTIONS.GET_ALL_VALID_CONFIGURATIONS);
    // creation of a handle for GET_ALL_REASONER-query
    ReasonerQueryHandle rqhandle = new arf.query.ReasonerQueryHandle(service.arf.ARF.QUERIES.GET_ALL_REASONER);

    // creation of a local ARF
    ARF arf = new ARF();
    // setting model handle to the ARF
    arf.setModel(mhandle);
    // setting input handle to the ARF
    arf.setInput(ihandle);
    // asking query to the ARF
    Information information = arf.ask(rqhandle, qhandle);
    // selecting reasoner handle of an optimal reasoner (BDDReasoner)
    List<ReasonerHandle> handles = information.getReasoner();
    ReasonerHandle rhandle = null;
    for (ReasonerHandle handle : handles) {
      if (handle.getAttributes().REASONING == ReasonerAttributes.OPTIMAL) {
        rhandle = handle;
        break;
      }
    }
    // setting reasoner handle to the ARF
    arf.setReasoner(rhandle);
    // asking question to the ARF
    Result result = arf.ask(qhandle);

    // casting result to question-dependent result
    ConfigurationResult cresult = (ConfigurationResult) result;
    // getting configurations from result
    List<Resource> answer = cresult.getAnswer();

    // getting log from result
    Log log = result.getLog();
    // getting protocol from log
    Protocol protocol = log.getProtocol();
    // getting statistics from log
    Statistics statistics = log.getStatistics();

    // print result of walkthrough
    print(answer, protocol, statistics);
  }
}
