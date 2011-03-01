package de.pure.diva.arf.ui.wizards;

import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import service.arf.question.Result;
import diva.Context;
import diva.Scenario;

public class ReasoningResultWizard extends Wizard {

  final String                                TITLE_INFO      = "Reasoning Protocol";
  final String                                MESSAGE_INFO    = "The protocol of the current reasoning task.";
  final String                                TITLE_RESULT    = "Reasoning Answer";
  final String                                MESSAGE_RESULT  = "The answer of the current reasoning task.";
  final String                                TITLE_LOGGING   = "Reasoning Statistics";
  final String                                MESSAGE_LOGGING = "The statistics of the current reasoning task.";
  final String                                TITLE           = "Reasoning Result";
  private Map<Scenario, Map<Context, Result>> results         = null;
  private String                              question        = null;
  private boolean                             logging         = false;

  @Override
  public boolean performFinish() {
    return true;
  }

  public ReasoningResultWizard(String question, Map<Scenario, Map<Context, Result>> results, boolean logging) {
    setWindowTitle(TITLE);
    this.question = question;
    this.results = results;
    this.logging = logging;
    setNeedsProgressMonitor(logging);
  }

  @Override
  public void addPages() {
    WizardPage resultPage = new ResultPage(TITLE_RESULT, MESSAGE_RESULT, this.results, this.question);
    WizardPage informationPage = new StatisticsPage(TITLE_INFO, MESSAGE_INFO, this.results);
    WizardPage loggingPage = new LoggingPage(TITLE_LOGGING, MESSAGE_LOGGING, this.results);
    addPage(resultPage);
    if (this.logging == true) {
      addPage(informationPage);
      addPage(loggingPage);
    }
  }
}
