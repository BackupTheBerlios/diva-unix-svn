package de.pure.diva.arf.ui.wizards;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.Wizard;

import service.arf.question.QuestionHandle;
import service.arf.reasoner.ReasonerHandle;

public class ReasonerWizard extends Wizard {

  private final static String TITLE_REASONER        = "Reasoner Selection";
  private final static String MESSAGE_REASONER      = "The ARF contains at this time the following listed reasoner. Please select one for answering the question.";
  private final static String TITLE_PARAMETER       = "Reasoning Parameters";
  private final static String MESSAGE_PARAMETER     = "The question can be parametrised. Please set the appropriate value to the parameter.";

  private ReasonerPage        reasonerPage          = null;
  private ReasonerHandle      selection             = null;
  private ParameterPage       reasonerParameterPage = null;
  private QuestionHandle      question              = null;
  private Resource            vmodel                = null;

  public ReasonerWizard(String title, Resource vmodel, QuestionHandle question) {
    super();
    setWindowTitle(title);
    this.vmodel = vmodel;
    this.question = question;
  }

  @Override
  public boolean performFinish() {
    this.selection = reasonerPage.getReasoner();
    return true;
  }

  @Override
  public void addPages() {
    reasonerParameterPage = new ParameterPage(TITLE_PARAMETER, MESSAGE_PARAMETER, this.question);
    addPage(reasonerParameterPage);
    reasonerPage = new ReasonerPage(TITLE_REASONER, MESSAGE_REASONER, this.vmodel, this.question);
    addPage(reasonerPage);
  }

  public ReasonerHandle getReasoner() {
    return this.selection;
  }
}
