package arf.question.question;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.Question;
import service.arf.question.QuestionAttributes;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import arf.question.BooleanResult;
import arf.question.QuestionService;

public class IsConfigQuestion extends QuestionService implements Question {

  private QuestionAttributes parameters = new QuestionAttributes();

  public IsConfigQuestion() {
    parameters.OPTIMISING = false;
    parameters.SOLVING = false;
    parameters.VALIDATING = true;
  }

  public Result ask(ReasonerHandle reasoner, ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = super.ask(reasoner, model, input, question, new BooleanResult());
    return result;
  }

  public String getName() {
    return ARF.QUESTIONS.IS_VALID_CONFIGURATION;
  }

  public QuestionAttributes getAttributes() {
    return this.parameters;
  }

  public String getQuestion() {
    return "Is configuration valid?";
  }

}
