package de.pure.diva.arf.wp2.simulator.question;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionAttributes;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import arf.question.ConfigurationResult;

public class GetAllSuitableConfigsQuestion extends WP2SimulatorQuestion {

  private QuestionAttributes parameters = new QuestionAttributes();

  public GetAllSuitableConfigsQuestion() {
    parameters.OPTIMISING = false;
    parameters.SOLVING = true;
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
    return WP2SimulatorQuestion.QUESTIONS.GET_ALL_SUITABLE_CONFIGURATIONS;
  }

  public String getQuestion() {
    return "Get all suitable configurations.";
  }
}
