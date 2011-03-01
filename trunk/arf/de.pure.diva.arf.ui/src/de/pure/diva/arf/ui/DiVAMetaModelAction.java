package de.pure.diva.arf.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import arf.util.Creator;
import arf.util.Loader;
import de.pure.diva.arf.ui.dialog.ReasoningResultWizardDialog;
import de.pure.diva.arf.ui.wizards.ReasonerSelector;
import de.pure.diva.arf.ui.wizards.ReasoningResultWizard;
import diva.Context;
import diva.Scenario;

public abstract class DiVAMetaModelAction implements IObjectActionDelegate {

  private IFile m_File = null;

  protected class ResultRunnable implements Runnable {

    private Map<Scenario, Map<Context, Result>> results  = null;
    private String                              question = null;
    private boolean                             logging  = false;

    public ResultRunnable(Map<Scenario, Map<Context, Result>> results, String question, boolean logging) {
      this.results = results;
      this.question = question;
      this.logging = logging;
    }

    public void run() {
      ReasoningResultWizard wiz = new ReasoningResultWizard(this.question, this.results, this.logging);
      ReasoningResultWizardDialog dialog = new ReasoningResultWizardDialog(null, wiz);
      dialog.create();
      dialog.open();
    }
  }

  protected void addResult(Map<Scenario, Map<Context, Result>> results, Scenario scenario, Context context, Result result) {
    Map<Context, Result> map = results.get(scenario);
    if (map == null) {
      map = new HashMap<Context, Result>();
      results.put(scenario, map);
    }
    map.put(context, result);
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

  protected ReasonerHandle getReasoner(Resource vmodel, String conf, QuestionHandle question) {
    ReasonerSelector selector = new ReasonerSelector(conf, question, vmodel);
    ReasonerHandle handle = selector.select();
    return handle;
  }

  public void selectionChanged(IAction action, ISelection selection) {
    m_File = null;
    if (selection instanceof IStructuredSelection && selection.isEmpty() == false) {
      m_File = (IFile) ((IStructuredSelection) selection).getFirstElement();
    }
  }

  public void run(IAction action) {
    // get the DiVA model resource
    IFile f = getDiVAModelFile();
    final Resource resource = getDiVAModelResource(f);
    if (resource != null) {
      final IFile file = f;
      try {
        IWorkbench wb = PlatformUI.getWorkbench();
        if (wb != null) {
          IWorkbenchWindow ww = wb.getActiveWorkbenchWindow();
          ww.run(true, false, new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) {
              try {
                DiVAMetaModelAction.this.run(monitor, resource);
                DiVAMetaModelAction.this.save(resource);
                file.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
              }
              catch (Throwable t) {
                new Creator().mergeFirstContext(resource, null);
                DiVAMetaModelAction.this.showSyncExceptionDialog(t);
              }
              monitor.done();
            }
          });
        }
      }
      catch (Throwable t) {
        showSyncExceptionDialog(t);
      }
    }
  }

  protected abstract void run(IProgressMonitor monitor, Resource vmodel) throws Exception;

  /**
   * Get the DiVA model workbench file.
   * 
   * @return The DiVA model workbench file.
   */
  protected IFile getDiVAModelFile() {
    IFile file = m_File;
    return file;
  }

  /**
   * Get the DiVA model resource.
   * 
   * @param file
   *          The DiVA model workbench file.
   * @return The DiVA model resource.
   */
  protected Resource getDiVAModelResource(IFile file) {
    Resource resource = null;
    if (file != null) {
      Loader loader = new Loader();
      resource = loader.load(file.getLocation().toString());
    }
    return resource;
  }

  protected void save(Resource resource) throws IOException {
    if (resource.isModified() == true) {
      // Save the contents of the resource to the file system.
      Map<Object, Object> options = new HashMap<Object, Object>();
      options.put(XMLResource.OPTION_ENCODING, "UTF-8");
      resource.save(options);
    }
  }

  /**
   * Show a dialog for the given exception and synchronize the dialog with the
   * display.
   * 
   * @param e
   *          The exception.
   */
  protected void showSyncExceptionDialog(Throwable e) {
    final String msg = e.getMessage() == null ? "Unknown application error: " + e.getClass() : e.getMessage();
    ARFUIActivator.getDefault().getLog().log(new Status(Status.ERROR, ARFUIActivator.PLUGIN_ID, msg, e));
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        MessageDialog.openError(null, "DiVA adaptation model action", msg);
      }
    });
  }

}
