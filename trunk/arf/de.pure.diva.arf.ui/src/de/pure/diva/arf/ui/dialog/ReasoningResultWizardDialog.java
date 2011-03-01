package de.pure.diva.arf.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ReasoningResultWizardDialog extends WizardDialog {

  public ReasoningResultWizardDialog(Shell parentShell, IWizard wizard) {
    super(parentShell, wizard);
  }

  protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
    if (label.equals(IDialogConstants.FINISH_LABEL)) {
      label = IDialogConstants.OK_LABEL;
    }
    return super.createButton(parent, id, label, defaultButton);
  }
}
