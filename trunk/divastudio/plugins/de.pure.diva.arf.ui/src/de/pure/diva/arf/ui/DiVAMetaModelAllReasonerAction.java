package de.pure.diva.arf.ui;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.widgets.Display;

import de.pure.diva.arf.ui.dialog.ReasonerDialog;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.query.Information;
import service.arf.query.ReasonerQueryHandle;
import service.arf.question.QuestionHandle;
import service.arf.reasoner.ReasonerHandle;

public class DiVAMetaModelAllReasonerAction extends DiVAMetaModelAction {

  @Override
  protected void run(IProgressMonitor monitor, Resource vmodel) throws Exception {
    ARF arf = new arf.ARF();
    ModelHandle model = new arf.ModelHandle(vmodel);
    InputHandle input = null;
    arf.setModel(model);
    arf.setInput(input);
    QuestionHandle question = null;
    ReasonerQueryHandle query = new arf.query.ReasonerQueryHandle(ARF.QUERIES.GET_ALL_REASONER);
    Information info = arf.ask(query, question);
    final List<ReasonerHandle> reasoner = info.getReasoner();
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        ReasonerDialog dialog = new ReasonerDialog(null, "All Reasoner", reasoner);
        dialog.open();
      }
    });
  }
}
