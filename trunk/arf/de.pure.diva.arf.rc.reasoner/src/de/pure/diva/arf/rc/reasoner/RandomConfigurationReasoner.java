package de.pure.diva.arf.rc.reasoner;

import org.osgi.framework.BundleActivator;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.Reasoner;
import arf.question.ConfigurationResult;

public class RandomConfigurationReasoner extends RandomReasoner implements Reasoner, BundleActivator {

  public static final String NAME = "Random Configuration Reasoner";

  public String getName() {
    return NAME;
  }

  protected Result solve(ModelHandle model, InputHandle input, QuestionHandle question) {
    Result result = null;
    if (ARF.QUESTIONS.ARE_THERE_VALID_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      RandomConfigurationSolver solver = new RandomConfigurationSolver(question.getParameters().TIME_CONSUMPTION, 1, RandomConfigurationSolver.NO, true);
      result = solver.exist(model, input);
    }
    else if (ARF.QUESTIONS.GET_ONE_VALID_CONFIGURATION.equals(question.getQuestion()) == true) {
      RandomConfigurationSolver solver = new RandomConfigurationSolver(question.getParameters().TIME_CONSUMPTION, 1, RandomConfigurationSolver.ALL, true);
      result = solver.solve(model, input);
    }
    else if (ARF.QUESTIONS.GET_BEST_VALID_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      RandomConfigurationSolver solver = new RandomConfigurationSolver(question.getParameters().TIME_CONSUMPTION, RandomConfigurationSolver.MAXCOUNT,
          RandomConfigurationSolver.BEST, true);
      result = solver.solve(model, input);
    }
    else if (ARF.QUESTIONS.GET_ALL_VALID_CONFIGURATIONS.equals(question.getQuestion()) == true) {
      RandomConfigurationSolver solver = new RandomConfigurationSolver(question.getParameters().TIME_CONSUMPTION, RandomConfigurationSolver.MAXCOUNT,
          RandomConfigurationSolver.ALL, true);
      result = solver.solve(model, input);
    }
    else {
      RandomConfigurationSolver solver = new RandomConfigurationSolver(question.getParameters().TIME_CONSUMPTION, 1, RandomConfigurationSolver.ALL, true);
      result = solver.solve(model, input);
    }
    return result;
  }

  public ConfigurationResult solve(ModelHandle model, InputHandle input) {
    RandomConfigurationSolver solver = new RandomConfigurationSolver(RandomConfigurationSolver.NOTIMEOUT, 1, RandomConfigurationSolver.ALL, true);
    return solver.solve(model, input);
  }
}
