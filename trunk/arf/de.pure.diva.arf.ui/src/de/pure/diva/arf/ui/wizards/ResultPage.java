package de.pure.diva.arf.ui.wizards;

import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import service.arf.question.Result;
import de.pure.diva.arf.ui.control.ReasonerResultComposite;
import diva.Context;
import diva.Scenario;

public class ResultPage extends WizardPage {

  private String                              title    = null;
  private Map<Scenario, Map<Context, Result>> results  = null;
  private String                              message  = null;
  private String                              question = null;

  protected ResultPage(String title, String message, Map<Scenario, Map<Context, Result>> results, String question) {
    super(title);
    this.message = message;
    this.title = title;
    this.results = results;
    this.question = question;
  }

  public void createControl(Composite parent) {
    setTitle(title);
    setMessage(message);

    ReasonerResultComposite composite = new ReasonerResultComposite(this.results, this.question);
    setControl(composite.createControl(parent));
  }
}
