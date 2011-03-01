package de.pure.diva.arf.ui.wizards;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import service.arf.question.Result;
import diva.Context;
import diva.Scenario;

public class LoggingPage extends WizardPage {

  private String                              message = null;
  private Map<Scenario, Map<Context, Result>> results = null;
  private String                              title   = null;
  private static Color                        WHITE   = new Color(null, 255, 255, 255);

  protected LoggingPage(String pageName) {
    super(pageName);
  }

  protected LoggingPage(String title, String message, Map<Scenario, Map<Context, Result>> results) {
    super(title);
    this.title = title;
    this.message = message;
    this.results = results;
  }

  public void createControl(Composite parent) {
    setTitle(title);
    setMessage(message);

    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout(1, true);
    composite.setLayout(gl);
    GridData data = new GridData(GridData.FILL_BOTH);
    composite.setLayoutData(data);

    TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
    data = new GridData(GridData.FILL_BOTH);
    data.widthHint = 450;
    data.heightHint = 200;
    tabFolder.setLayoutData(data);

    // Info tab
    TabItem infoTab = new TabItem(tabFolder, SWT.NULL);
    infoTab.setText("Information ");

    Text infoText = new Text(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
    infoText.setEditable(false);
    infoText.setBackground(LoggingPage.WHITE);
    String infos = addLoggingInformations(IMessageProvider.INFORMATION);
    if (infos.equals("")) {
      infos = "No information";
    }
    infoText.append(infos);
    infoTab.setControl(infoText);

    // Warnings tab
    TabItem warningsTab = new TabItem(tabFolder, SWT.NULL);
    warningsTab.setText("Warnings ");

    Text warningsText = new Text(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
    warningsText.setEditable(false);
    warningsText.setBackground(LoggingPage.WHITE);
    String warnings = addLoggingInformations(IMessageProvider.WARNING);
    if (warnings.equals("")) {
      warnings = "No warnings";
    }
    warningsText.append(warnings);
    warningsTab.setControl(warningsText);

    // error tab
    TabItem errorTab = new TabItem(tabFolder, SWT.NULL);
    errorTab.setText("Errors ");

    Text errorText = new Text(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
    errorText.setEditable(false);
    errorText.setBackground(LoggingPage.WHITE);
    String errors = addLoggingInformations(IMessageProvider.ERROR);
    if (errors.equals("")) {
      errors = "No errors";
    }
    errorText.append(errors);
    errorTab.setControl(errorText);

    setControl(composite);
  }

  private String addLoggingInformations(int type) {
    String output = "";
    for (Object s : results.keySet()) {
      if (s instanceof Scenario) {
        Scenario sc = (Scenario) s;
        Map<Context, Result> conMap = results.get(sc);
        for (Object c : conMap.keySet()) {
          if (c instanceof Context) {
            Context con = (Context) c;
            Result result = (Result) conMap.get(con);
            List<String> input = null;
            if (type == IMessageProvider.INFORMATION) {
              input = result.getLog().getStatistics().getInfos();
            }
            if (type == IMessageProvider.WARNING) {
              input = result.getLog().getStatistics().getWarnings();
            }
            if (type == IMessageProvider.ERROR) {
              input = result.getLog().getStatistics().getErrors();
            }
            for (String string : input) {
              output = output + string + "\n\n---------------------\n\n";
            }
          }
        }
      }
    }
    return output;
  }
}
