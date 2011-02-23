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

public class DiVAMetaModelBestConfiguration extends DiVAMetaModelAction {

  @Override
  protected void run(IProgressMonitor monitor, Resource vmodel) throws Exception {
    QuestionHandle question = new arf.question.QuestionHandle(ARF.QUESTIONS.GET_BEST_VALID_CONFIGURATIONS);
    ReasonerHandle reasoner = getReasoner(vmodel, "Best Configuration", question);
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
        addResult(results, scenario, context, result);
        creator.mergeFirstContext(vmodel, contexts.get(resource));
      }
      monitor.done();
      boolean logging = question.getParameters().LOGGING;
      ResultRunnable runnable = new ResultRunnable(results, "Get Best Configuration", logging);
      Display.getDefault().syncExec(runnable);
    }
  }
}
