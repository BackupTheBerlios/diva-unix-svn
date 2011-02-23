package de.pure.diva.arf.pm.reasoner.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import service.arf.model.Mapper;
import arf.model.ARFContext;
import arf.model.ARFModel;
import de.pure.diva.arf.alloy.sat.AlloySATSolver;
import de.pure.diva.arf.alloy.sat.util.ARFModel2AlloyGenerator;
import de.pure.diva.arf.alloy.sat.util.AlloySolutionListener;

public class ModelPartitioner {

  public static class Solving extends AlloySATSolver.Solving {
    public Solving(String solving) {
      super(solving);
    }
  }

  public static final Solving BEST  = new Solving(AlloySATSolver.BEST.getSolving());
  public static final Solving NEXT  = new Solving(AlloySATSolver.NEXT.getSolving());
  public static final Solving ALL   = new Solving(AlloySATSolver.ALL.getSolving());
  public static final Solving NO    = new Solving(AlloySATSolver.NO.getSolving());
  public static final Solving COUNT = new Solving("count");

  class Partitioner extends Thread {
    class Solver {

      private ARFModel              model    = null;
      private ARFContext            context  = null;
      private AlloySolutionListener listener = null;

      private AlloySATSolver        solver   = null;

      public Solver(ARFModel arfModel, ARFContext arfContext, AlloySATSolver solver, AlloySolutionListener listener) {
        this.model = arfModel;
        this.context = arfContext;
        this.listener = listener;
        this.solver = solver;
      }

      public void synchronise() throws Exception {
        try {
          this.solver.sychronise(this.listener);
        }
        finally {
          Partitioner.this.listeners.remove(this.listener);
        }
      }

      public void solve() throws Throwable {
        ARFModel2AlloyGenerator aGenerator = new ARFModel2AlloyGenerator();
        String system = aGenerator.generate(this.model);
        String context = aGenerator.select(this.model, this.context);
        this.solver.solve(system, context, this.listener);
      }
    }

    private Mapper                        mapper      = null;
    private Solving                       solving     = null;

    private ARFModel                      model       = null;
    private ARFContext                    context     = null;
    private int                           maxcount    = 0;
    private ModelPartitionerListener      listener    = null;
    private boolean                       interrupted = false;

    private ColourMarker                  marker      = null;

    private Set<PMRAlloySolutionListener> listeners   = Collections.synchronizedSet(new HashSet<PMRAlloySolutionListener>());

    public Partitioner(Mapper mapper, Solving solving, ARFModel arfModel, ARFContext arfContext, int count, ModelPartitionerListener listener) {
      this.mapper = mapper;
      this.solving = solving;
      this.model = arfModel;
      this.context = arfContext;
      this.maxcount = count;
      this.listener = listener;
      this.marker = new ColourMarker();
      super.setName("ModelPartitioner");
    }

    @Override
    public synchronized void interrupt() {
      for (PMRAlloySolutionListener listener : new HashSet<PMRAlloySolutionListener>(this.listeners)) {
        listener.finish();
      }
      this.interrupted = true;
      super.interrupt();
    }

    @Override
    public boolean isInterrupted() {
      return this.interrupted || super.isInterrupted();
    }

    public synchronized void setCount(int count) {
      ModelPartitioner.this.count = count;
    }

    @Override
    public void run() {
      try {
        if (this.model != null) {
          Map<String, ARFModel> colour2modelMap = this.marker.mark(this.model, this.context);

          if (isInterrupted() == false) {
            Set<String> colours = colour2modelMap.keySet();
            ExistResultMerger existMerger = new ExistResultMerger(this, this.listener, colours);
            CountResultMerger countMerger = new CountResultMerger(this, this.listener, colours);
            ConfigurationResultMerger configurationMerger = new ConfigurationResultMerger(this, this.listener, colours, this.model);
            Set<Solver> solvers = new HashSet<Solver>();
            for (String colour : colours) {
              ARFModel model = colour2modelMap.get(colour);
              AlloySATSolver alloy = null;
              PMRAlloySolutionListener listener = null;
              if (this.solving.getSolving().equals(NO.getSolving()) == true) {
                alloy = new AlloySATSolver(AlloySATSolver.NOTIMEOUT, this.maxcount, AlloySATSolver.NO);
                listener = new ExistAlloySolutionListener(existMerger, colour, alloy.connector, this.mapper, model, this.context);
              }
              else if (this.solving.getSolving().equals(COUNT.getSolving()) == true) {
                alloy = new AlloySATSolver(AlloySATSolver.NOTIMEOUT, this.maxcount, AlloySATSolver.ALL);
                listener = new CountAlloySolutionListener(countMerger, colour, alloy.connector, this.mapper, model, this.context);
              }
              else {
                alloy = new AlloySATSolver(AlloySATSolver.NOTIMEOUT, this.maxcount, AlloySATSolver.ALL);
                listener = new AllAlloySolutionListener(configurationMerger, colour, alloy.connector, this.mapper, model, this.context);
              }
              Solver solver = new Solver(model, this.context, alloy, listener);
              this.listeners.add(listener);
              solvers.add(solver);
              solver.solve();
            }
            for (Solver solver : solvers) {
              solver.synchronise();
            }
          }
        }
        else {
          this.listener.setFault("Application error:\n\nNo problem defined.");
        }
      }
      catch (Throwable e) {
        String msg = e.getMessage() == null ? "Unknown application error: " + e.getClass() : "Application error:\n\n" + e.getMessage();
        this.listener.setFault(msg);
      }
      finally {
        for (PMRAlloySolutionListener listener : new HashSet<PMRAlloySolutionListener>(this.listeners)) {
          listener.finish();
        }
      }
    }
  }

  private Mapper      mapper      = null;
  private Solving     solving     = null;

  private int         count       = -1;
  private Object      lock        = new Object();
  private Partitioner partitioner = null;

  public ModelPartitioner(Mapper mapper, Solving solving) {
    this.mapper = mapper;
    this.solving = solving;
  }

  public int count() {
    return this.count;
  }

  public void cancel(ModelPartitionerListener listener) {
    synchronized (this.lock) {
      if (this.partitioner != null) {
        this.partitioner.interrupt();
        this.partitioner = null;
      }
    }
  }

  public void partition(ARFModel arfModel, ARFContext arfContext, int count, ModelPartitionerListener listener) {
    this.count = 0;
    synchronized (this.lock) {
      this.partitioner = new Partitioner(this.mapper, this.solving, arfModel, arfContext, count, listener);
      this.partitioner.start();
    }
  }
}
