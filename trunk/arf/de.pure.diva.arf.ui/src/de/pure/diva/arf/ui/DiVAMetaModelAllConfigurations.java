package de.pure.diva.arf.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.widgets.Display;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;
import arf.util.Cleaner;
import arf.util.Creator;
import arf.util.Loader;
import diva.Context;
import diva.Scenario;

public class DiVAMetaModelAllConfigurations extends DiVAMetaModelAction {

  /**
   * Run the Reasoning update of the given model.
   * 
   * @param monitor
   *          The progress monitor.
   * @param model
   *          The DiVA model.
   * @throws Exception
   */
  protected void run(IProgressMonitor monitor, Resource vmodel) throws Exception {
    QuestionHandle question = new arf.question.QuestionHandle(ARF.QUESTIONS.GET_ALL_VALID_CONFIGURATIONS);
    ReasonerHandle reasoner = getReasoner(vmodel, "All Configurations", question);
    if (reasoner != null) {
      if (reasoner.getReasoner() == ARF.REASONER.UNKNOWN) {
        reasoner = null;
      }
      monitor.beginTask("Perform reasoning", IProgressMonitor.UNKNOWN);
      Cleaner cleaner = new Cleaner();
      cleaner.clean(vmodel);
      Loader loader = new Loader();
      ARF arf = new arf.ARF();
      ModelHandle model = new arf.ModelHandle(vmodel);
      InputHandle input = new arf.InputHandle(loader);
      arf.setReasoner(reasoner);
      arf.setModel(model);
      arf.setInput(input);
      Creator creator = new Creator();
      Map<Resource, EObject> contexts = creator.createContexts(vmodel);
      Map<Scenario, Map<Context, Result>> results = new HashMap<Scenario, Map<Context, Result>>();
      for (Resource resource : contexts.keySet()) {
        input.setContext(resource);
        Result result = arf.ask(question);
        Context context = (Context) contexts.get(resource);
        Scenario scenario = (Scenario) context.eContainer();
        creator.mergeFirstContext(vmodel, contexts.get(resource));
        addResult(results, scenario, context, result);
        if (vmodel.isModified() == false) {
          break;
        }
      }
      monitor.done();
      boolean logging = question.getParameters().LOGGING;
      ResultRunnable runnable = new ResultRunnable(results, "Get All Configurations", logging);
      Display.getDefault().syncExec(runnable);
    }
  }
}
