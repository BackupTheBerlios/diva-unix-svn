package de.pure.diva.arf.wp2.simulator.reasoner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import arf.reasoner.ReasonerService;
import de.pure.diva.arf.wp2.simulator.question.WP2SimulatorQuestion;

public class WP2SimulatorReasoner extends ReasonerService implements Reasoner {

  public static final String              NAME      = "Alloy/Kermeta Reasoner (WP2 Simulator)";

  private static final ReasonerAttributes PARAMETER = new ReasonerAttributes();

  public WP2SimulatorReasoner() {
    PARAMETER.TIME = ReasonerAttributes.SLOW;
    PARAMETER.MEMORY = ReasonerAttributes.MEDIUM;
    PARAMETER.REASONING = ReasonerAttributes.OPTIMAL;
  }

  public ReasonerAttributes getAttributes() {
    return PARAMETER;
  }

  public String getName() {
    return NAME;
  }

  public List<String> getSupportedHandles() {
    return Arrays.asList(Reasoner.ALL_HANDLES_SUPPORTED);
  }

  public List<String> getSupportedQuestions() {
    List<String> questions = new ArrayList<String>();
    questions.add(ARF.QUESTIONS.GET_ALL_VALID_CONFIGURATIONS);
    questions.add(WP2SimulatorQuestion.QUESTIONS.PERFORM_COMPLETE_SIMULATION);
    questions.add(WP2SimulatorQuestion.QUESTIONS.GET_ALL_SUITABLE_CONFIGURATIONS);
    questions.add(WP2SimulatorQuestion.QUESTIONS.COMPUTE_PROPERTY_PRIORITIES);
    questions.add(WP2SimulatorQuestion.QUESTIONS.COMPUTE_CONFIGURATION_SCORES);
    questions.add(WP2SimulatorQuestion.QUESTIONS.COMPUTE_CONFIGURATION_VERDICTS);
    return questions;
  }

  public Boolean optimising() {
    return Boolean.TRUE;
  }

  public Result reason(ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = null;
    if (question.getParameters().OPTIMISING == Boolean.TRUE && (question.getParameters().SOLVING == Boolean.FALSE || solving() == false)) {
      result = optimise(model, input, question);
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

  public Boolean solving() {
    return Boolean.TRUE;
  }

  public Boolean validating() {
    return Boolean.FALSE;
  }

  protected Result optimise(ModelHandle model, InputHandle input, QuestionHandle question) {
    String method = null;
    if (WP2SimulatorQuestion.QUESTIONS.COMPUTE_PROPERTY_PRIORITIES.equals(question.getQuestion()) == true) {
      method = WP2Simulator.POPULATE_PRIORITIES_METHOD;
    }
    else if (WP2SimulatorQuestion.QUESTIONS.COMPUTE_CONFIGURATION_SCORES.equals(question.getQuestion()) == true) {
      method = WP2Simulator.POPULATE_SCORES_METHOD;
    }
    else if (WP2SimulatorQuestion.QUESTIONS.COMPUTE_CONFIGURATION_VERDICTS.equals(question.getQuestion()) == true) {
      method = WP2Simulator.POPULATE_VERDICTS_METHOD;
    }
    WP2Simulator simulator = new WP2Simulator(method);
    ConfigurationResult cresult = simulator.solve(model, input);
    return cresult;
  }

  public ConfigurationResult optimise(ModelHandle model, InputHandle input) {
    WP2Simulator simulator = new WP2Simulator(WP2Simulator.POPULATE_PRIORITIES_METHOD);
    ConfigurationResult cresult = simulator.solve(model, input);
    simulator = new WP2Simulator(WP2Simulator.POPULATE_SCORES_METHOD);
    cresult = simulator.solve(model, input);
    simulator = new WP2Simulator(WP2Simulator.POPULATE_VERDICTS_METHOD);
    cresult = simulator.solve(model, input);
    return cresult;
  }

  protected Result solve(ModelHandle model, InputHandle input, QuestionHandle question) {
    String method = null;
    if (ARF.QUESTIONS.GET_ALL_VALID_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      method = WP2Simulator.POPULATE_COMPLETE_SIMULATION_METHOD;
    }
    else if (WP2SimulatorQuestion.QUESTIONS.PERFORM_COMPLETE_SIMULATION.equals(question.getQuestion()) == true) {
      method = WP2Simulator.POPULATE_COMPLETE_SIMULATION_METHOD;
    }
    else if (WP2SimulatorQuestion.QUESTIONS.GET_ALL_SUITABLE_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      method = WP2Simulator.POPULATE_VALID_CONFIGURATIONS_METHOD;
    }
    WP2Simulator simulator = new WP2Simulator(method);
    ConfigurationResult cresult = simulator.solve(model, input);
    return cresult;
  }

  public ConfigurationResult solve(ModelHandle model, InputHandle input) {
    WP2Simulator simulator = new WP2Simulator(WP2Simulator.POPULATE_VALID_CONFIGURATIONS_METHOD);
    ConfigurationResult cresult = simulator.solve(model, input);
    return cresult;
  }

  public BooleanResult validate(ModelHandle model, InputHandle input) {
    BooleanResult result = new BooleanResult();
    String error = UnsupportedOperationException.class.getName() + ": " + new UnsupportedOperationException().getLocalizedMessage();
    result.getLog().getStatistics().getErrors().add(error);
    return result;
  }

}
