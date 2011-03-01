package arf.question;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.ReasoningParameters;
import service.arf.question.Question;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.Reasoner;
import service.arf.reasoner.ReasonerHandle;
import arf.reasoner.ReasonerManager;

public abstract class AbstractQuestion implements Question {

  public Result ask(ReasonerHandle reasoner, ModelHandle model, InputHandle input, QuestionHandle question, Result dResult) {
    long start = System.currentTimeMillis();
    Reasoner r = ReasonerManager.getInstance().getReasoner(reasoner.getReasoner());
    Result result = r.reason(model, input, question);
    long stop = System.currentTimeMillis();
    result = result == null ? new UnknownResult() : result;
    String error = null;
    try {
      result.getClass().asSubclass(dResult.getClass());
    }
    catch (Exception e) {
      error = "[" + getName() + " (" + dResult.getClass().getName() + ")] - " + e.getClass().getName() + ": " + e.getLocalizedMessage();
    }
    if (error != null) {
      dResult.setLog(result.getLog());
      result = dResult;
    }
    if (question.getParameters().LOGGING == true) {
      if (error != null) {
        result.getLog().getStatistics().getErrors().add(error);
      }
      ReasoningParameters parameters = new ReasoningParameters();
      parameters.LOGGING = question.getParameters().LOGGING;
      parameters.OPTIMISING = question.getParameters().OPTIMISING;
      parameters.SOLVING = question.getParameters().SOLVING;
      parameters.VALIDATING = question.getParameters().VALIDATING;
      parameters.TIME_CONSUMPTION = (stop - start);
      result.getLog().getProtocol().addUsedReasoner(reasoner, parameters);
    }
    return result;
  }

}
