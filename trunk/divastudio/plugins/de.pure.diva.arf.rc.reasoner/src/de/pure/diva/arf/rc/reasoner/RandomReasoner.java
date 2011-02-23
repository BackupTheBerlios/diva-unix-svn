package de.pure.diva.arf.rc.reasoner;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.Reasoner;
import service.arf.reasoner.ReasonerAttributes;
import arf.question.BooleanResult;
import arf.question.ConfigurationResult;
import arf.question.UnknownResult;
import arf.reasoner.ReasonerBundleActivator;

public abstract class RandomReasoner extends ReasonerBundleActivator implements Reasoner, BundleActivator {

  private static final ReasonerAttributes PARAMETER = new ReasonerAttributes();

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext context) throws Exception {
    super.start(context);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception {
    super.stop(context);
  }

  public RandomReasoner() {
    PARAMETER.TIME = ReasonerAttributes.SLOW;
    PARAMETER.MEMORY = ReasonerAttributes.MEDIUM;
    PARAMETER.REASONING = ReasonerAttributes.NONE_OPTIMAL;
  }

  public ReasonerAttributes getAttributes() {
    return PARAMETER;
  }

  public List<String> getSupportedHandles() {
    List<String> handles = new ArrayList<String>();
    handles.add(arf.ModelHandle.class.getName());
    handles.add(arf.InputHandle.class.getName());
    return handles;
  }

  public List<String> getSupportedQuestions() {
    List<String> questions = new ArrayList<String>();
    questions.add(ARF.QUESTIONS.ARE_THERE_VALID_CONFIGURATIONS);
    questions.add(ARF.QUESTIONS.GET_ALL_VALID_CONFIGURATIONS);
    questions.add(ARF.QUESTIONS.GET_ONE_VALID_CONFIGURATION);
    questions.add(ARF.QUESTIONS.GET_BEST_VALID_CONFIGURATIONS);
    return questions;
  }

  public Boolean optimising() {
    return Boolean.FALSE;
  }

  public Result reason(ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = null;
    if (question.getParameters().OPTIMISING == Boolean.TRUE && (question.getParameters().SOLVING == Boolean.FALSE || solving() == false)) {
      result = optimise(model, input);
    }
    else if (question.getParameters().SOLVING == Boolean.TRUE) {
      result = solve(model, input, question);
    }
    else if (question.getParameters().VALIDATING == Boolean.TRUE) {
      result = validate(model, input);
    }
    else {
      result = new UnknownResult();
      String error = "Question " + question.getQuestion() + " is not supported by reasoner " + getName() + "";
      result.getLog().getStatistics().getErrors().add(error);
    }
    return result;
  }

  protected abstract Result solve(ModelHandle model, InputHandle input, QuestionHandle question);

  public Boolean solving() {
    return Boolean.TRUE;
  }

  public Boolean validating() {
    return Boolean.FALSE;
  }

  public ConfigurationResult optimise(ModelHandle model, InputHandle input) {
    ConfigurationResult result = new ConfigurationResult();
    String error = UnsupportedOperationException.class.getName() + ": " + new UnsupportedOperationException().getLocalizedMessage();
    result.getLog().getStatistics().getErrors().add(error);
    return result;
  }

  public BooleanResult validate(ModelHandle model, InputHandle input) {
    BooleanResult result = new BooleanResult();
    String error = UnsupportedOperationException.class.getName() + ": " + new UnsupportedOperationException().getLocalizedMessage();
    result.getLog().getStatistics().getErrors().add(error);
    return result;
  }

}
