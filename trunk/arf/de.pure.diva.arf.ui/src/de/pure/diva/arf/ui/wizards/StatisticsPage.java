package de.pure.diva.arf.ui.wizards;

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
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import service.arf.ReasoningParameters;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import diva.Context;
import diva.Scenario;

public class StatisticsPage extends WizardPage {

  private static final int                    MEMORY    = 1;
  private static final int                    TIME      = 0;
  private Map<Scenario, Map<Context, Result>> resultMap = null;
  private String                              title     = null;
  private String                              message   = null;

  protected StatisticsPage(String title, String message, Map<Scenario, Map<Context, Result>> resultMap) {
    super(title);
    this.message = message;
    this.title = title;
    this.resultMap = resultMap;
  }

  public void createControl(Composite parent) {
    setTitle(title);
    setMessage(message);

    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout(1, true);
    composite.setLayout(gl);
    GridData data = new GridData(GridData.FILL_BOTH);
    composite.setLayoutData(data);

    Label time = new Label(composite, SWT.NONE);
    time.setText("Total time comsumption (s): " + getConsumptions(StatisticsPage.TIME));

    Label memory = new Label(composite, SWT.NONE);
    memory.setText("Total memory usage (MB): " + getConsumptions(StatisticsPage.MEMORY));

    @SuppressWarnings("unused")
    Label dummy = new Label(composite, SWT.NONE);

    Label reasoner = new Label(composite, SWT.NONE);
    reasoner.setText("Used reasoner: ");

    TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.V_SCROLL);
    data = new GridData(GridData.FILL_BOTH);
    data.heightHint = 100;
    createColumns(tableViewer);
    tableViewer.getTable().setLayoutData(data);
    tableViewer.getTable().setHeaderVisible(true);
    tableViewer.getTable().setLinesVisible(true);
    tableViewer.setContentProvider(new IStructuredContentProvider() {

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
              Map<Context, Result> conMap = (Map<Context, Result>) map.get(sc);
              Map<String, String[]> temp = new HashMap<String, String[]>();
              for (Object c : conMap.keySet()) {
                if (c instanceof Context) {
                  Context con = (Context) c;
                  Result re = (Result) conMap.get(con);
                  List<ReasonerHandle> handels = re.getLog().getProtocol().getUsedReasoner();
                  String[] res = new String[3];
                  for (ReasonerHandle reasonerHandle : handels) {
                    res[0] = reasonerHandle.getReasoner();
                    Long time = re.getLog().getProtocol().getParameters(reasonerHandle).TIME_CONSUMPTION;
                    Float t = new Float(time);
                    if (time != ReasoningParameters.UNKNOWN) {
                      t = t / 1000;
                    }
                    res[1] = t.toString();
                    res[2] = new Float(re.getLog().getProtocol().getParameters(reasonerHandle).MEMORY_USAGE).toString();
                    String conname = con.getName();
                    if ((conname == null) || (conname.equals(""))) {
                      conname = con.getId();
                    }
                    if ((conname == null) || (conname.equals(""))) {
                      conname = "Context " + "" + contextCount;
                    }
                    int index = 0;
                    while (temp.containsKey(conname) == true) {
                      conname = conname + "" + index;
                      index++;
                    }
                    temp.put(conname, res);
                  }
                  contextCount++;
                }
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

      public void dispose() {}

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
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
        String temp = ((String[]) element)[columnIndex];
        if (temp == null || temp.equals("-1.0")) {
          temp = "Don't Know";
        }
        return temp;
      }
    });
    tableViewer.setInput(this.resultMap);

    tableViewer.getTable().pack();
    setControl(composite);
  }

  private String getConsumptions(int type) {
    String output = "Don't Know";
    Long mem = new Long(0);

    for (Object s : resultMap.keySet()) {
      if (s instanceof Scenario) {
        Scenario sc = (Scenario) s;
        Map<Context, Result> conMap = resultMap.get(sc);
        for (Object c : conMap.keySet()) {
          if (c instanceof Context) {
            Context con = (Context) c;
            Result re = (Result) conMap.get(con);
            Long temp = null;
            if (type == StatisticsPage.MEMORY) {
              temp = re.getLog().getProtocol().getParameters().MEMORY_USAGE;
            }
            if (type == StatisticsPage.TIME) {
              temp = re.getLog().getProtocol().getParameters().TIME_CONSUMPTION;
            }
            mem = mem + temp;
            if (temp == ReasoningParameters.UNKNOWN) {
              mem = new Long(-1);
              break;
            }
          }
        }
        if (mem > 0) {
          if (type == StatisticsPage.TIME) {
            Float fl = new Float(mem);
            fl = fl / 1000;
            output = fl.toString();
          }
          if (type == StatisticsPage.MEMORY) {
            output = mem.toString();
          }
        }
      }
    }
    return output;
  }

  private void createColumns(TableViewer tableViewer) {
    String[] titles = { "Reasoner", "Time consumption (s)", "Memory usage (MB)" };

    for (int i = 0; i < titles.length; i++) {
      TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT, i);
      column.getColumn().setWidth(200);
      column.getColumn().setText(titles[i]);
      column.getColumn().setResizable(false);
    }
  }
}
