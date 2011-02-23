package de.pure.diva.arf.wp2.simulator.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IObjectActionDelegate;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.model.Loader;
import service.arf.question.QuestionHandle;
import service.arf.reasoner.ReasonerHandle;
import de.pure.diva.arf.ui.DiVAMetaModelAction;
import de.pure.diva.arf.wp2.simulator.question.WP2SimulatorQuestion;
import de.pure.diva.arf.wp2.simulator.reasoner.WP2SimulatorReasoner;

public class GetAllSuitableConfigsAction extends DiVAMetaModelAction implements IObjectActionDelegate {

  protected void run(IProgressMonitor monitor, Resource vmodel) throws Exception {
    ReasonerHandle reasoner = new arf.reasoner.ReasonerHandle(WP2SimulatorReasoner.NAME);
    monitor.beginTask("Perform reasoning", IProgressMonitor.UNKNOWN);
    arf.util.Cleaner cleaner = new arf.util.Cleaner();
    cleaner.clean(vmodel);
    Loader loader = new arf.util.Loader();
    ARF arf = new arf.ARF();
    ModelHandle model = new arf.ModelHandle(vmodel);
    InputHandle input = new arf.InputHandle(loader);
    QuestionHandle question = new arf.question.QuestionHandle(WP2SimulatorQuestion.QUESTIONS.GET_ALL_SUITABLE_CONFIGURATIONS);
    arf.setReasoner(reasoner);
    arf.setModel(model);
    arf.setInput(input);
    arf.ask(question);
  }

}
