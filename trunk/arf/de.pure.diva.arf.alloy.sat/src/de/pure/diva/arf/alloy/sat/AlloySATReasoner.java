package de.pure.diva.arf.alloy.sat;

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

public class AlloySATReasoner extends ReasonerBundleActivator implements Reasoner, BundleActivator {

  public static final String              NAME      = "Alloy SAT Reasoner";

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

  public AlloySATReasoner() {
    PARAMETER.TIME = ReasonerAttributes.SLOW;
    PARAMETER.MEMORY = ReasonerAttributes.MEDIUM;
    PARAMETER.REASONING = ReasonerAttributes.OPTIMAL;
  }

  public String getName() {
    return NAME;
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
    questions.add(ARF.QUESTIONS.GET_BEST_VALID_CONFIGURATIONS);
    questions.add(ARF.QUESTIONS.GET_ONE_VALID_CONFIGURATION);
    questions.add(ARF.QUESTIONS.HOW_MANY_VALID_CONFIGURATIONS);
    questions.add(ARF.QUESTIONS.GET_NEXT_VALID_CONFIGURATION);
    questions.add(ARF.QUESTIONS.IS_VALID_CONFIGURATION);
    return questions;
  }

  public Boolean optimising() {
    return Boolean.TRUE;
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
      result = validate(model, input, question);
    }
    else {
      result = new UnknownResult();
      String error = "Question " + question.getQuestion() + " is not supported by reasoner " + getName() + "";
      result.getLog().getStatistics().getErrors().add(error);
    }
    return result;
  }

  public Boolean solving() {
    return Boolean.TRUE;
  }

  public Boolean validating() {
    return Boolean.TRUE;
  }

  public ConfigurationResult optimise(ModelHandle model, InputHandle input) {
    ConfigurationResult result = new ConfigurationResult();
    String error = UnsupportedOperationException.class.getName() + ": " + new UnsupportedOperationException().getLocalizedMessage();
    result.getLog().getStatistics().getErrors().add(error);
    return result;
  }

  protected Result solve(ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = null;
    if (ARF.QUESTIONS.ARE_THERE_VALID_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      AlloySATSolver solver = new AlloySATSolver(question.getParameters().TIME_CONSUMPTION, 1, AlloySATSolver.NO);
      result = solver.exist(model, input);
    }
    else if (ARF.QUESTIONS.GET_ONE_VALID_CONFIGURATION.equals(question.getQuestion()) == true) {
      AlloySATSolver solver = new AlloySATSolver(question.getParameters().TIME_CONSUMPTION, 1, AlloySATSolver.ALL);
      result = solver.solve(model, input);
    }
    else if (ARF.QUESTIONS.GET_BEST_VALID_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      AlloySATSolver solver = new AlloySATSolver(question.getParameters().TIME_CONSUMPTION, AlloySATSolver.MAXCOUNT, AlloySATSolver.BEST);
      result = solver.solve(model, input);
    }
    else if (ARF.QUESTIONS.HOW_MANY_VALID_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      AlloySATSolver solver = new AlloySATSolver(question.getParameters().TIME_CONSUMPTION, AlloySATSolver.MAXCOUNT, AlloySATSolver.ALL);
      result = solver.count(model, input);
    }
    else if (ARF.QUESTIONS.GET_NEXT_VALID_CONFIGURATION.equals(question.getQuestion()) == true) {
      AlloySATSolver solver = new AlloySATSolver(question.getParameters().TIME_CONSUMPTION, AlloySATSolver.MAXCOUNT, AlloySATSolver.NEXT);
      result = solver.solve(model, input);
    }
    else {
      AlloySATSolver solver = new AlloySATSolver(question.getParameters().TIME_CONSUMPTION, AlloySATSolver.MAXCOUNT, AlloySATSolver.ALL);
      result = solver.solve(model, input);
    }
    return result;
  }

  public ConfigurationResult solve(ModelHandle model, InputHandle input) {
    AlloySATSolver solver = new AlloySATSolver(AlloySATSolver.NOTIMEOUT, AlloySATSolver.MAXCOUNT, AlloySATSolver.ALL);
    return solver.solve(model, input);
  }

  protected Result validate(ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = null;
    if (result != null) {}
    else {
      AlloySATSolver solver = new AlloySATSolver(AlloySATSolver.NOTIMEOUT, 1, AlloySATSolver.ALL);
      result = solver.check(model, input);
    }
    return result;
  }

  public BooleanResult validate(ModelHandle model, InputHandle input) {
    AlloySATSolver solver = new AlloySATSolver(AlloySATSolver.NOTIMEOUT, 1, AlloySATSolver.NO);
    return solver.check(model, input);
  }
}
