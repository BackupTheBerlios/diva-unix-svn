package de.pure.diva.arf.ui.wizards;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import service.arf.question.QuestionHandle;
import service.arf.reasoner.ReasonerHandle;

public class ReasonerSelector {

  class ReasonerRunnable implements Runnable {
    private String         title     = null;
    private ReasonerHandle selection = null;
    private QuestionHandle question  = null;
    private Resource       vmodel    = null;

    public ReasonerRunnable(String title, Resource vmodel, QuestionHandle question) {
      this.title = title;
      this.question = question;
      this.vmodel = vmodel;
    }

    public void run() {

      ReasonerWizard wiz = new ReasonerWizard(this.title, this.vmodel, this.question);
      WizardDialog dialog = new WizardDialog(null, wiz);
      dialog.create();
      dialog.open();
      if (dialog.getReturnCode() == WizardDialog.OK) {
        this.selection = wiz.getReasoner();
      }
    }

    public ReasonerHandle getReasoner() {
      return this.selection;
    }
  }

  private String         title    = null;
  private QuestionHandle question = null;
  private Resource       vmodel   = null;

  public ReasonerSelector(String title, QuestionHandle question, Resource vmodel) {
    this.title = title;
    this.question = question;
    this.vmodel = vmodel;
  }

  public ReasonerHandle select() {
    ReasonerRunnable runnable = new ReasonerRunnable(this.title, this.vmodel, this.question);
    Display.getDefault().syncExec(runnable);
    return runnable.getReasoner();
  }
}
