package de.pure.diva.arf.ui.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import service.arf.reasoner.ReasonerHandle;

public class ReasonerDialog extends TitleAreaDialog {

  private static Color         WHITE        = new Color(null, 255, 255, 255);

  private List<ReasonerHandle> reasoner     = null;

  private Composite            listing      = null;
  private String               title        = null;

  public static int            SHOW_MODE    = 1;

  private final static String  SHOW_TITEL   = "ARF Reasoner Listing";
  private final static String  SHOW_MESSAGE = "The ARF contains at this time the following listed reasoner.";

  public ReasonerDialog(Shell parentShell, String title, List<ReasonerHandle> reasoner) {
    super(parentShell);
    this.reasoner = reasoner;
    this.title = title;
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    getShell().setText(this.title);
    setTitle(SHOW_TITEL);
    setMessage(SHOW_MESSAGE);

    // create the top level composite for the dialog area
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    layout.verticalSpacing = 0;
    layout.horizontalSpacing = 0;
    composite.setLayout(layout);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    composite.setFont(parent.getFont());
    Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
    titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    this.listing = new Composite(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
    this.listing.setLayout(new GridLayout());
    this.listing.setLayoutData(new GridData(GridData.FILL_BOTH));
    this.listing.setFont(parent.getFont());
    this.listing.setBackground(ReasonerDialog.WHITE);
    for (ReasonerHandle r : this.reasoner) {
      Text text = new Text(this.listing, SWT.NONE);
      text.setBackground(this.listing.getBackground());
      text.setText(r.getReasoner());
    }
    return composite;
  }
}
