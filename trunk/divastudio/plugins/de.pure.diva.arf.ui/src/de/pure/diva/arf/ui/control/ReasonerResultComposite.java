package de.pure.diva.arf.ui.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import service.arf.question.Result;
import diva.Context;
import diva.Scenario;

public class ReasonerResultComposite {

  private Map<Scenario, Map<Context, Result>> result   = null;
  private String                              question = null;

  public ReasonerResultComposite(Map<Scenario, Map<Context, Result>> result, String question) {
    this.result = result;
    this.question = question;
  }

  public Composite createControl(Composite parent) {
    // create the top level composite for the dialog area
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    layout.verticalSpacing = 0;
    layout.horizontalSpacing = 0;
    layout.numColumns = 1;
    composite.setLayout(layout);
    GridData data = new GridData(GridData.FILL_BOTH);
    composite.setLayoutData(data);
    composite.setFont(parent.getFont());
    Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
    titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.V_SCROLL);
    data = new GridData(GridData.FILL_BOTH);
    data.heightHint = 150;
    createColumns(tableViewer);
    tableViewer.getTable().setLayoutData(data);
    tableViewer.getTable().setHeaderVisible(true);
    tableViewer.getTable().setLinesVisible(true);
    tableViewer.setContentProvider(new IStructuredContentProvider() {

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

      public void dispose() {}

      @SuppressWarnings("unchecked")
      public Object[] getElements(Object inputElement) {
        Object[] result = null;
        int scenarioCount = 1;
        int contextCount = 1;
        ArrayList<String[]> r = null;
        if (inputElement instanceof Map<?, ?>) {
          r = new ArrayList<String[]>();
          Map<?, ?> map = (Map<?, ?>) inputElement;
          for (Object s : map.keySet()) {
            if (s instanceof Scenario) {
              contextCount = 1;
              Scenario sc = (Scenario) s;
              Map<?, ?> conMap = (Map<?, ?>) map.get(sc);
              Map<String, String[]> temp = new HashMap<String, String[]>();
              for (Object c : conMap.keySet()) {
                if (c instanceof Context) {
                  Context con = (Context) c;
                  Result re = (Result) conMap.get(con);
                  Object obj = re.getAnswer();
                  Object answer;
                  if (obj instanceof List<?>) {
                    answer = ((List<Object>) obj).size();
                  }
                  else {
                    answer = obj;
                  }
                  String[] res = new String[3];
                  res[0] = sc.getName();
                  if ((res[0] == null) || (res[0].equals(""))) {
                    res[0] = sc.getId();
                  }
                  if ((res[0] == null) || (res[0].equals(""))) {
                    res[0] = "Scenario " + "" + scenarioCount;
                  }
                  res[1] = con.getName();
                  if ((res[1] == null) || (res[1].equals(""))) {
                    res[1] = con.getId();
                  }
                  if ((res[1] == null) || (res[1].equals(""))) {
                    res[1] = "Context " + "" + contextCount;
                  }
                  res[2] = answer.toString();
                  temp.put(res[1], res);
                }
                contextCount++;
              }
              // sort elements by context
              Object[] sorting = temp.keySet().toArray();
              Arrays.sort(sorting);
              for (Object object : sorting) {
                r.add(temp.get(object));
              }
              scenarioCount++;
            }
          }
        }
        result = r.toArray(new Object[r.size()]);
        return result;
      }
    });
    tableViewer.setLabelProvider(new ITableLabelProvider() {

      public void removeListener(ILabelProviderListener listener) {}

      public boolean isLabelProperty(Object element, String property) {
        return false;
      }

      public void dispose() {}

      public void addListener(ILabelProviderListener listener) {}

      public Image getColumnImage(Object element, int columnIndex) {
        return null;
      }

      public String getColumnText(Object element, int columnIndex) {
        return ((String[]) element)[columnIndex];
      }
    });
    tableViewer.setInput(result);
    tableViewer.getTable().pack();
    return composite;
  }

  public void createColumns(TableViewer tableViewer) {
    String[] titles = { "Scenario", "Context", this.question };

    for (int i = 0; i < titles.length; i++) {
      TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT, i);
      column.getColumn().setWidth(200);
      column.getColumn().setText(titles[i]);
      column.getColumn().setResizable(false);
    }
  }

}
