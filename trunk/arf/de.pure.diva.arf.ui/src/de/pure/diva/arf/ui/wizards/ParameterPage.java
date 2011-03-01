package de.pure.diva.arf.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

import service.arf.ReasoningParameters;
import service.arf.question.QuestionHandle;

public class ParameterPage extends WizardPage {

  private String          message       = null;
  private String          title         = null;
  private Long            timeout       = ReasoningParameters.DONT_CARE;
  private boolean         logging       = Boolean.FALSE;
  private Text            timeoutText   = null;
  private TimeoutListener timeListener  = null;
  private Slider          timeoutSlider = null;
  private Button          timeoutButton = null;
  private QuestionHandle  question      = null;

  class TimeoutListener implements ModifyListener {

    public void modifyText(ModifyEvent e) {
      String time = timeoutText.getText();
      try {
        if (time != null) {
          timeout = new Long(time);
        }
      }
      catch (IllegalArgumentException exc) {}
      finally {
        validatePage();
        if (isPageComplete() == true) {
          timeoutSlider.setSelection(timeout.intValue());
        }
      }
    }
  }

  protected ParameterPage(String title) {
    super(title);
  }

  protected ParameterPage(String title, String message, QuestionHandle question) {
    super(title);
    this.title = title;
    this.message = message;
    this.question = question;
  }

  private void setParameters() {
    this.question.getParameters().LOGGING = this.logging;
    Long time = this.timeout;
    if (time != ReasoningParameters.DONT_CARE) {
      time = time * new Long(1000);
    }
    this.question.getParameters().TIME_CONSUMPTION = time;
  }

  @Override
  public boolean canFlipToNextPage() {
    if (getErrorMessage() != null) {
      return true;
    }
    else {
      return super.canFlipToNextPage();
    }
  }

  public void createControl(Composite parent) {
    setTitle(this.title);
    setMessage(this.message);

    // create the top level composite for the wizard area
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 3;
    composite.setLayout(layout);
    GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
    data.verticalAlignment = SWT.END;
    composite.setLayoutData(data);
    composite.setFont(parent.getFont());

    // Timeout textfield
    timeoutButton = new Button(composite, SWT.CHECK);
    timeoutButton.setText("Timeout in s");
    timeoutButton.setSelection(false);
    timeoutButton.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent e) {
        if (timeoutButton.getSelection() == false) {
          timeoutSlider.setEnabled(false);
          timeoutText.setEnabled(false);
          timeout = ReasoningParameters.DONT_CARE;
        }
        else {
          timeoutSlider.setEnabled(true);
          timeoutText.setEnabled(true);
          try {
            timeout = new Long(timeoutText.getText());
          }
          catch (IllegalArgumentException ex) {}
        }
        validatePage();
      }

      public void widgetDefaultSelected(SelectionEvent e) {}
    });

    // Timeout slider
    timeoutSlider = new Slider(composite, SWT.HORIZONTAL);
    timeoutSlider.setMinimum(0);
    timeoutSlider.setMaximum(3610);
    timeoutSlider.setIncrement(1);
    timeoutSlider.setPageIncrement(100);
    timeoutSlider.setSelection(300);
    timeoutSlider.setEnabled(false);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.grabExcessHorizontalSpace = true;
    timeoutSlider.setLayoutData(data);

    timeoutSlider.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent e) {
        timeoutText.removeModifyListener(timeListener);
        int time = ((Slider) e.getSource()).getSelection();
        timeout = new Long(time);
        timeoutText.setText(timeout.toString());
        validatePage();
        timeoutText.addModifyListener(timeListener);
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });

    timeoutText = new Text(composite, SWT.BORDER);
    data = new GridData();
    data.widthHint = 40;
    timeoutText.setLayoutData(data);
    timeoutText.setText("300");
    timeoutText.setToolTipText("Give time in s");
    timeListener = new TimeoutListener();
    timeoutText.addModifyListener(timeListener);
    timeoutText.setEnabled(false);

    // Logging enablement Button
    final Button logg = new Button(composite, SWT.CHECK);
    logg.setSelection(false);
    logg.setText("Logging");
    logg.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent e) {
        logging = logg.getSelection();
        validatePage();
      }

      public void widgetDefaultSelected(SelectionEvent e) {}
    });

    setControl(composite);
    validatePage();
  }

  private void validatePage() {
    setPageComplete(false);
    boolean completed = true;

    String time = timeoutText.getText();

    if (timeoutButton.getSelection() == true) {
      try {
        Long temp = new Long(time);
        if (temp < 0) {
          setErrorMessage("Timeout have to be a positiv number between 0 and 3600");
          completed = false;
        }
        if (temp > 3600) {
          setErrorMessage("Timeout have to be a positiv number between 0 and 3600");
          completed = false;
        }
      }
      catch (Exception e) {
        setErrorMessage("Timeout have to be a positiv number between 0 and 3600");
        completed = false;
      }
    }
    if (completed == true) {
      setErrorMessage(null);
      setMessage(this.message);
      setPageComplete(true);
      setParameters();
    }
  }

  public Long getTimeout() {
    if (timeout <= 0) {
      timeout = ReasoningParameters.DONT_CARE;
    }
    return this.timeout;
  }

  public boolean getLogging() {
    return this.logging;
  }

}
