package de.pure.diva.arf.wp2.simulator.reasoner;

import org.eclipse.core.runtime.Status;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.reasoner.Solver;
import arf.question.ConfigurationResult;
import de.pure.diva.arf.wp2.simulator.WP2SimulatorActivator;
import diva.ksimulator.KExecMain;
import fr.irisa.triskell.eclipse.console.EclipseConsole;
import fr.irisa.triskell.eclipse.console.IOConsole;

public class WP2Simulator implements Solver {

  public static final String POPULATE_COMPLETE_SIMULATION_METHOD  = "populateCompleteSimulation";
  public static final String POPULATE_VALID_CONFIGURATIONS_METHOD = "populateValidConfigurations";
  public static final String POPULATE_PRIORITIES_METHOD           = "populatePriorities";
  public static final String POPULATE_SCORES_METHOD               = "populateScores";
  public static final String POPULATE_VERDICTS_METHOD             = "populateVerdicts";

  public static final String DIVA_KERMETA_CODE                    = "platform:/plugin/diva.model/kermeta/Main.kmt";

  private String             method                               = null;

  public WP2Simulator(String method) {
    this.method = method;
  }

  public ConfigurationResult solve(ModelHandle model, InputHandle input) {
    ConfigurationResult result = new ConfigurationResult();
    String uri = model.getModelURI();
    IOConsole console = new EclipseConsole("DiVA Simulator");
    try {
      KExecMain.run(method, uri, console);
    }
    catch (Throwable e) {
      String msg = e.getMessage() == null ? "Unknown application error: " + e.getClass() : e.getMessage();
      result.getLog().getStatistics().getErrors().add(msg);
      WP2SimulatorActivator.getDefault().getLog().log(new Status(Status.ERROR, WP2SimulatorActivator.PLUGIN_ID, msg, e));
    }
    return result;
  }

}
