package arf.question.question;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.Question;
import service.arf.question.QuestionAttributes;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import arf.question.NumericResult;
import arf.question.QuestionService;

public class HowManyConfigsQuestion extends QuestionService implements Question {

  private QuestionAttributes parameters = new QuestionAttributes();

  public HowManyConfigsQuestion() {
    parameters.OPTIMISING = false;
    parameters.SOLVING = true;
    parameters.VALIDATING = false;
  }

  public Result ask(ReasonerHandle reasoner, ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = super.ask(reasoner, model, input, question, new NumericResult());
    return result;
  }

  public String getName() {
    return ARF.QUESTIONS.HOW_MANY_VALID_CONFIGURATIONS;
  }

  public QuestionAttributes getAttributes() {
    return this.parameters;
  }

  public String getQuestion() {
    return "How many valid configurations do exist?";
  }

}
