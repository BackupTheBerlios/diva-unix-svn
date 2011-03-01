package arf.question.question;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.Question;
import service.arf.question.QuestionAttributes;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import arf.question.ConfigurationResult;
import arf.question.QuestionService;

public class GetAllConfigsQuestion extends QuestionService implements Question {

  private QuestionAttributes parameters = new QuestionAttributes();

  public GetAllConfigsQuestion() {
    parameters.OPTIMISING = true;
    parameters.SOLVING = true;
    parameters.VALIDATING = false;
  }

  public Result ask(ReasonerHandle reasoner, ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = super.ask(reasoner, model, input, question, new ConfigurationResult());
    return result;
  }

  public String getName() {
    return ARF.QUESTIONS.GET_ALL_VALID_CONFIGURATIONS;
  }

  public QuestionAttributes getAttributes() {
    return this.parameters;
  }

  public String getQuestion() {
    return "Get all valid configurations.";
  }

}
