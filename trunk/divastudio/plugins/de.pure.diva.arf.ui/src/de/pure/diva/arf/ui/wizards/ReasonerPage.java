package de.pure.diva.arf.ui.wizards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.query.Information;
import service.arf.query.ReasonerQueryHandle;
import service.arf.question.QuestionHandle;
import service.arf.reasoner.ReasonerHandle;

public class ReasonerPage extends WizardPage {

  private String                      title     = null;
  private String                      message   = null;
  private Button                      selection = null;
  private Composite                   listing   = null;
  private Map<Button, ReasonerHandle> map       = new HashMap<Button, ReasonerHandle>();
  private List<ReasonerHandle>        reasoner  = null;
  private Resource                    vmodel    = null;
  private QuestionHandle              question  = null;
  private Composite                   composite = null;
  private Button                      b         = null;
  private String                      rea       = "";

  private static Color                WHITE     = new Color(null, 255, 255, 255);
  private static Color                GRAY      = new Color(null, 212, 210, 206);

  protected ReasonerPage(String title, String message, Resource vmodel, QuestionHandle question) {
    super(title);
    this.title = title;
    this.message = message;
    this.vmodel = vmodel;
    this.question = question;
  }

  @Override
  public void setVisible(boolean visible) {
    clearMap();
    askForReasoner();

    // remove existing listing and create new one
    if (this.listing != null) {
      this.listing.dispose();
    }

    this.listing = new Composite(this.composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
    this.listing.setLayout(new GridLayout());
    this.listing.setLayoutData(new GridData(GridData.FILL_BOTH));
    this.listing.setFont(this.composite.getFont());
    this.listing.setBackground(ReasonerPage.GRAY);

    Button button = null;
    for (ReasonerHandle r : this.reasoner) {

      Button but = new Button(this.listing, SWT.RADIO);
      if (button == null) {
        button = but;
      }
      but.setText(r.getReasoner());
      but.setBackground(this.listing.getBackground());
      map.put(but, r);
      but.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          Object source = e.getSource();
          if (source instanceof Button) {
            if (((Button) source).getSelection() == true) {
              ReasonerPage.this.selection = (Button) source;
              ReasonerPage.this.rea = ((Button) source).getText();
            }
          }
        }
      });

      if (r.getReasoner().equals(rea)) {
        but.setSelection(true);
        if (b.getSelection() == false) {
          this.selection = but;
        }
      }
      but.setEnabled(false);
    }
    if (rea.equals("")) {
      button.setSelection(true);
      rea = button.getText();
    }
    enableListing();

    this.composite.redraw();
    this.composite.update();
    this.composite.layout();
    super.setVisible(visible);
  }

  private void askForReasoner() {
    ARF arf = new arf.ARF();
    ModelHandle model = new arf.ModelHandle(this.vmodel);
    InputHandle input = null;
    arf.setModel(model);
    arf.setInput(input);
    ReasonerQueryHandle query = new arf.query.ReasonerQueryHandle(ARF.QUERIES.GET_ALL_REASONER);
    Information info = arf.ask(query, this.question);
    this.reasoner = info.getReasoner();
  }

  private void enableListing() {
    if (selection != b) {
      ReasonerPage.this.listing.setEnabled(true);
      ReasonerPage.this.listing.setBackground(ReasonerPage.WHITE);
      for (Button button : ReasonerPage.this.map.keySet()) {
        if (button != b) {
          button.setEnabled(true);
          button.setBackground(ReasonerPage.this.listing.getBackground());
        }
      }
    }
  }

  private void clearMap() {
    Object[] keys = map.keySet().toArray();
    for (Object but : keys) {
      if ((Button) but != b) {
        map.remove(but);
      }
    }
  }

  public void createControl(Composite parent) {
    setTitle(this.title);
    setMessage(this.message);

    // create the top level composite for the wizard area
    this.composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout(1, true);
    this.composite.setLayout(layout);
    this.composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    this.composite.setFont(parent.getFont());

    // Add possibility for automatically selection
    b = new Button(this.composite, SWT.CHECK);
    b.setText("Automatically selected by ARF");
    b.setEnabled(true);
    b.setBackground(this.composite.getBackground());
    map.put(b, new arf.reasoner.ReasonerHandle(ARF.REASONER.UNKNOWN));
    b.addSelectionListener(new SelectionAdapter() {
      private Button selection = null;

      @Override
      public void widgetSelected(SelectionEvent e) {
        for (Button but : map.keySet()) {
          if (but.getText().equals(rea)) {
            selection = but;
          }
        }
        Object source = e.getSource();
        if (source instanceof Button) {
          if (((Button) source).getSelection() == true) {
            this.selection = ReasonerPage.this.selection;
            ReasonerPage.this.selection = (Button) source;
            ReasonerPage.this.listing.setEnabled(false);
            ReasonerPage.this.listing.setBackground(ReasonerPage.GRAY);
            for (Button button : ReasonerPage.this.map.keySet()) {
              if (button != source) {
                button.setEnabled(false);
                button.setBackground(ReasonerPage.this.listing.getBackground());
              }
            }
          }
          else {
            ReasonerPage.this.selection = this.selection;
            ReasonerPage.this.listing.setEnabled(true);
            ReasonerPage.this.listing.setBackground(ReasonerPage.WHITE);
            for (Button button : ReasonerPage.this.map.keySet()) {
              if (button != source) {
                button.setEnabled(true);
                button.setBackground(ReasonerPage.this.listing.getBackground());
              }
            }
          }
        }
      }
    });
    b.setSelection(true);
    ReasonerPage.this.selection = b;
    setControl(this.composite);
    validatePage();

  }

  private void validatePage() {
    setErrorMessage(null);
    setMessage(this.message);
    setPageComplete(true);

  }

  public ReasonerHandle getReasoner() {
    return this.map.get(this.selection);
  }

}
