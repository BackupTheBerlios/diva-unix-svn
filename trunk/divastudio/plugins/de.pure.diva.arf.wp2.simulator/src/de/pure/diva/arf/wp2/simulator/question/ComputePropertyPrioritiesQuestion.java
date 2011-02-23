package de.pure.diva.arf.wp2.simulator.question;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionAttributes;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import arf.question.ConfigurationResult;

public class ComputePropertyPrioritiesQuestion extends WP2SimulatorQuestion {

  private QuestionAttributes parameters = new QuestionAttributes();

  public ComputePropertyPrioritiesQuestion() {
    parameters.OPTIMISING = true;
    parameters.SOLVING = false;
    parameters.VALIDATING = false;
  }

  public Result ask(ReasonerHandle reasoner, ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = super.ask(reasoner, model, input, question, new ConfigurationResult());
    return result;
  }

  public QuestionAttributes getAttributes() {
    return this.parameters;
  }

  public String getName() {
    return WP2SimulatorQuestion.QUESTIONS.COMPUTE_PROPERTY_PRIORITIES;
  }

  public String getQuestion() {
    return "Compute property priorities.";
  }
}
