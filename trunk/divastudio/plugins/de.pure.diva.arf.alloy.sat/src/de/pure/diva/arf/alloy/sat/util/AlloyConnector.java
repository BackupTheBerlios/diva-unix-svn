package de.pure.diva.arf.alloy.sat.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;
import de.pure.diva.arf.alloy.sat.AlloySATConstants;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.parser.Module;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

public class AlloyConnector {

  public static boolean CREATE_MODULE_INMEMORY = false;

  private class Alloy extends Thread {
    private String                 system       = null;
    private String                 context      = null;
    private int                    maxcount     = AlloySATConstants.ALLOY_SOLUTION_MAX_COUNT;
    private boolean                checking     = false;
    private AlloyConnectorListener listener     = null;
    private boolean                interrupted  = false;
    File                           alloy_system = null;

    public Alloy(String system, String context, int maxcount, boolean checking, AlloyConnectorListener listener) {
      this.system = system;
      this.context = context;
      this.maxcount = maxcount;
      this.checking = checking;
      this.listener = listener;
      super.setName("AlloyConnector");
    }

    @Override
    public void interrupt() {
      this.interrupted = true;
      super.interrupt();
    }

    @Override
    public boolean isInterrupted() {
      return this.interrupted || super.isInterrupted();
    }

    @Override
    public void run() {
      try {
        AlloyConnector.this.count = -1;
        A4Solution solution = solve(this.system, this.context, this.checking);
        if (solution.satisfiable() == true) {
          AlloyConnector.this.count = 0;
          if (this.checking == false) {
            Map<String, List<String>> configurations = new HashMap<String, List<String>>();
            while (this.isInterrupted() == false && solution.satisfiable() == true) {
              List<String> atoms = addSolution(configurations, solution);
              if (atoms != null) {
                AlloyConnector.this.count = AlloyConnector.this.count + 1;
                this.listener.addAtoms(atoms);
              }
              if (this.maxcount == AlloySATConstants.ALLOY_SOLUTION_MAX_COUNT || configurations.size() < this.maxcount) {
                solution = solution.next();
              }
              else {
                break;
              }
            }
          }
          this.listener.setSuccess();
        }
        else {
          this.listener.setFault("Application error:\n\nProblem not satisfiable.");
        }
      }
      catch (Throwable e) {
        String msg = e.getMessage() == null ? "Unknown application error: " + e.getClass() : "Application error:\n\n" + e.getMessage();
        this.listener.setFault(msg);
      }
      finally {
        if (alloy_system != null) {
          try {
            alloy_system.delete();
          }
          catch (Throwable t) {}
          finally {
            alloy_system = null;
          }
        }
      }
      AlloyConnector.this.alloys.remove(this.listener);
    }

    private A4Solution solve(String system, String context, boolean checking) throws IOException, Err {
      // chooses the Alloy4 options
      A4Options opt = new A4Options();
      opt.solver = A4Options.SatSolver.SAT4J;

      Module world = null;
      if (CREATE_MODULE_INMEMORY == false) {
        world = createModuleFromFile(system, context);
      }
      else {
        world = createModuleFromString(system, context);
      }
      A4Solution solution = TranslateAlloyToKodkod.execute_command(new A4Reporter(), world.getAllReachableSigs(), world.getAllCommands().get(0), opt);
      return solution;
    }

    private Module createModuleFromFile(String system, String context) throws IOException, Err {
      // create a temporary file
      alloy_system = File.createTempFile(ALLOY_FILE_PREFIX, ALLOY_FILE_SUFFIX);
      alloy_system.deleteOnExit();
      FileWriter fw = new FileWriter(alloy_system);
      fw.write(system);
      fw.write("\n");
      if (checking == true) {
        fw.write("run {" + context + "}");
      }
      else {
        fw.write("run {" + context + "}");
      }
      fw.close();

      Module world = CompUtil.parseEverything_fromFile(new A4Reporter(), null, alloy_system.getAbsolutePath());
      return world;
    }

    private Module createModuleFromString(String system, String context) throws IOException, Err {
      StringBuffer buffer = new StringBuffer();
      buffer.append(system);
      buffer.append("\n");
      buffer.append("run {");
      buffer.append(context);
      buffer.append("}");

      String filename = "";
      Map<String, String> map = new LinkedHashMap<String, String>();
      map.put(filename, buffer.toString());

      Module world = CompUtil.parseEverything_fromFile(new A4Reporter(), map, filename);
      return world;
    }

    private List<String> addSolution(Map<String, List<String>> solutions, A4Solution solution) {
      Iterator<ExprVar> atoms = solution.getAllAtoms().iterator();
      List<String> list = new ArrayList<String>();
      while (atoms.hasNext()) {
        ExprVar v = atoms.next();
        String atom = v.label;
        list.add(atom);
      }
      Collections.sort(list, new Comparator<String>() {
        public int compare(String arg0, String arg1) {
          return arg0.compareTo(arg1);
        }
      });
      String hash = list.toString();
      List<String> result = null;
      if (solutions.containsKey(hash) == false) {
        result = new ArrayList<String>();
        solutions.put(hash, list);
        result.addAll(list);
      }
      return result;
    }

  }

  private static final String                ALLOY_FILE_PREFIX = "alloy_system";
  private static final String                ALLOY_FILE_SUFFIX = ".als";

  private ARFModel2AlloyGenerator            aGenerator        = new ARFModel2AlloyGenerator();
  private int                                count             = -1;
  private Map<AlloyConnectorListener, Alloy> alloys            = Collections.synchronizedMap(new HashMap<AlloyConnectorListener, Alloy>());

  public int count() {
    return this.count;
  }

  public void cancel(AlloyConnectorListener listener) {
    Alloy alloy = this.alloys.get(listener);
    if (alloy != null) {
      alloy.interrupt();
      this.alloys.remove(listener);
    }
  }

  public void solve(String system, String context, int maxcount, boolean checking, AlloyConnectorListener listener) throws Err, IOException {
    Alloy alloy = new Alloy(system, context, maxcount, checking, listener);
    this.alloys.put(listener, alloy);
    alloy.start();
  }

  public void solve(ARFModel model, int maxcount, boolean checking, AlloyConnectorListener listener) throws Err, IOException {
    String system = aGenerator.generate(model);
    String values = aGenerator.select(model, (ARFContext) null);
    solve(system, values, maxcount, checking, listener);
  }

  public void solve(ARFModel model, ARFContext context, int maxcount, boolean checking, AlloyConnectorListener listener) throws Err, IOException {
    String system = aGenerator.generate(model);
    String values = aGenerator.select(model, context);
    solve(system, values, maxcount, checking, listener);
  }

  public void solve(ARFModel model, ARFConfiguration configuration, int maxcount, boolean checking, AlloyConnectorListener listener) throws Err, IOException {
    String system = aGenerator.generate(model);
    String values = aGenerator.select(model, configuration);
    solve(system, values, maxcount, checking, listener);
  }
}
