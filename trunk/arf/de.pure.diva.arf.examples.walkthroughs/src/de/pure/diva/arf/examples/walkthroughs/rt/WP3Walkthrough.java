package de.pure.diva.arf.examples.walkthroughs.rt;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import arf.ARF;
import arf.question.BooleanResult;
import arf.question.ConfigurationResult;
import de.pure.diva.arf.examples.walkthroughs.Walkthrough;

/**
 * The WP3 walkthrough as described in the Deliverable D4.2.
 */
public class WP3Walkthrough extends Walkthrough {

  /**
   * Dummy method for getting the WP3 reasoning model.
   * 
   * @return The DiVA variability model which is <b>null</b>.
   */
  private Resource getVariabilityModel() {
    return null;
  }

  /**
   * Dummy method for getting the SAT reasoner.
   * 
   * @return The SAT reasoner handle which contains an unknown reasoner link.
   */
  private ReasonerHandle getSATReasoner() {
    // return new arf.reasoner.ReasonerHandle(AlloySATReasoner.NAME);
    return new arf.reasoner.ReasonerHandle("Alloy SAT Reasoner");
  }

  @Override
  public void run() {
    // getting variability model
    Resource vm = getVariabilityModel();
    // creation of model handle with variability model
    ModelHandle mhandle = new arf.ModelHandle(vm);
    // getting context model
    Resource context = null;
    // creation of input handle with context
    InputHandle ihandle = new arf.InputHandle(context, null);
    // creation of a handle for GET_ONE_VALID_CONFIGURATION-question
    QuestionHandle qhandle = new arf.question.QuestionHandle(service.arf.ARF.QUESTIONS.GET_ONE_VALID_CONFIGURATION);
    // setting TIME-parameter of question handle to 60 seconds
    qhandle.getParameters().TIME_CONSUMPTION = new Long(60000);
    // getting SAT reasoner handle
    ReasonerHandle rhandle = getSATReasoner();

    // creation of a local ARF
    ARF arf = new ARF();
    // setting reasoner handle to the ARF
    arf.setReasoner(rhandle);
    // setting model handle to the ARF
    arf.setModel(mhandle);
    // setting input handle to the ARF
    arf.setInput(ihandle);
    // asking question to the ARF
    Result result = arf.ask(qhandle);

    // casting result to question-dependent result
    ConfigurationResult cresult = (ConfigurationResult) result;
    // getting first configuration from result
    Resource configuration = cresult.getAnswer().size() == 0 ? null : cresult.getAnswer().get(0);

    // setting configuration to input handle
    ihandle.setConfiguration(configuration);
    // creation of a handle for IS_VALID_CONFIGURATION-question
    qhandle = new arf.question.QuestionHandle(service.arf.ARF.QUESTIONS.IS_VALID_CONFIGURATION);

    // setting no reasoner to the ARF
    arf.setReasoner(null);
    // asking question to the ARF
    result = arf.ask(qhandle);

    // casting result to question-dependent result
    BooleanResult bresult = (BooleanResult) result;
    // getting Boolean value from result
    Boolean answer = bresult.getAnswer();

    // print result of walkthrough
    print(answer, null, null);
  }
}
