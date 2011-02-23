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

public class AreThereConfigsQuestion extends QuestionService implements Question {

  private QuestionAttributes parameters = new QuestionAttributes();

  public AreThereConfigsQuestion() {
    parameters.OPTIMISING = false;
    parameters.SOLVING = true;
    parameters.VALIDATING = false;
  }

  public Result ask(ReasonerHandle reasoner, ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = super.ask(reasoner, model, input, question, new BooleanResult());
    return result;
  }

  public String getName() {
    return ARF.QUESTIONS.ARE_THERE_VALID_CONFIGURATIONS;
  }

  public QuestionAttributes getAttributes() {
    return this.parameters;
  }

  public String getQuestion() {
    return "Are there valid configurations?";
  }

}
