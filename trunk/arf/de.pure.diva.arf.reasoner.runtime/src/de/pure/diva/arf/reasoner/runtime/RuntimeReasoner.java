package de.pure.diva.arf.reasoner.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerAttributes;
import arf.question.BooleanResult;
import arf.question.ConfigurationResult;
import arf.question.NumericResult;
import arf.question.UnknownResult;
import arf.reasoner.DummySolver;
import arf.reasoner.ReasonerBundleActivator;

public class RuntimeReasoner extends ReasonerBundleActivator implements BundleActivator {

  public static final String              NAME      = "Runtime Reasoner";

  private static final ReasonerAttributes PARAMETER = new ReasonerAttributes();

  private DummySolver                     solver    = new DummySolver();

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

  public RuntimeReasoner() {
    PARAMETER.TIME = ReasonerAttributes.UNKNOWN;
    PARAMETER.MEMORY = ReasonerAttributes.UNKNOWN;
    PARAMETER.REASONING = ReasonerAttributes.UNKNOWN;
  }

  public String getName() {
    return NAME;
  }

  public ReasonerAttributes getAttributes() {
    return PARAMETER;
  }

  public List<String> getSupportedHandles() {
    return Arrays.asList(ALL_HANDLES_SUPPORTED);
  }

  public List<String> getSupportedQuestions() {
    return new ArrayList<String>();// Arrays.asList(ALL_QUESTIONS_SUPPORTED);
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
      ConfigurationResult cresult = solve(model, input);
      result = getResult(cresult, question);
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

  private Result getResult(ConfigurationResult result, QuestionHandle question) {
    Result r = result;
    if (ARF.QUESTIONS.ARE_THERE_VALID_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      r = new BooleanResult();
      Boolean answer = result.getAnswer().isEmpty() == false;
      ((BooleanResult) r).setAnswer(answer);
      r.setLog(result.getLog());
    }
    else if (ARF.QUESTIONS.HOW_MANY_VALID_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      r = new NumericResult();
      int answer = result.getAnswer().size();
      ((NumericResult) r).setAnswer(answer);
      r.setLog(result.getLog());
    }
    return r;
  }

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

  public ConfigurationResult solve(ModelHandle model, InputHandle input) {
    return this.solver.solve(model, input);
  }

  public BooleanResult validate(ModelHandle model, InputHandle input) {
    BooleanResult result = new BooleanResult();
    String error = UnsupportedOperationException.class.getName() + ": " + new UnsupportedOperationException().getLocalizedMessage();
    result.getLog().getStatistics().getErrors().add(error);
    return result;
  }

}
