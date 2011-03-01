package de.pure.diva.arf.examples.walkthroughs.re;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.ModelHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import arf.ARF;
import arf.question.BooleanResult;
import de.pure.diva.arf.examples.walkthroughs.Walkthrough;

/**
 * The WP1 walkthrough as described in the Deliverable D4.2.
 */
public class WP1Walkthrough extends Walkthrough {

  /**
   * Dummy method for getting the WP1 reasoning model.
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
    // creation of model handle with variability model
    ModelHandle mhandle = new arf.ModelHandle(vm);
    // creation of a handle for ARE_THERE_VALID_CONFIGURATIONS-question
    QuestionHandle qhandle = new arf.question.QuestionHandle(service.arf.ARF.QUESTIONS.ARE_THERE_VALID_CONFIGURATIONS);

    // creation of a local ARF
    ARF arf = new ARF();
    // setting model handle to the ARF
    arf.setModel(mhandle);
    // asking question to the ARF
    Result result = arf.ask(qhandle);

    // casting result to question-dependent result
    BooleanResult bresult = (BooleanResult) result;
    // getting Boolean value from result
    Boolean answer = bresult.getAnswer();

    // print result of walkthrough
    print(answer, null, null);
  }

}
