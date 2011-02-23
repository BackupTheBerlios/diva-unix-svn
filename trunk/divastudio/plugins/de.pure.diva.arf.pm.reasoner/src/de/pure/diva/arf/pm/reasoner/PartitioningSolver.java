package de.pure.diva.arf.pm.reasoner;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.Result;
import service.arf.reasoner.Solver;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.util.Mapper;
import arf.question.BooleanResult;
import arf.question.ConfigurationResult;
import arf.question.NumericResult;
import de.pure.diva.arf.pm.reasoner.util.AllPMRConfigurationListener;
import de.pure.diva.arf.pm.reasoner.util.BestPMRConfigurationListener;
import de.pure.diva.arf.pm.reasoner.util.CountPMRConfigurationListener;
import de.pure.diva.arf.pm.reasoner.util.ModelPartitioner;
import de.pure.diva.arf.pm.reasoner.util.ModelPartitionerListener;
import de.pure.diva.arf.pm.reasoner.util.NextPMRConfigurationListener;

public class PartitioningSolver implements Solver {

  public static class Solving extends ModelPartitioner.Solving {
    public Solving(String solving) {
      super(solving);
    }
  }

  public static final Solving BEST        = new Solving(ModelPartitioner.BEST.getSolving());
  public static final Solving NEXT        = new Solving(ModelPartitioner.NEXT.getSolving());
  public static final Solving ALL         = new Solving(ModelPartitioner.ALL.getSolving());
  public static final Solving NO          = new Solving(ModelPartitioner.NO.getSolving());
  public static final Solving COUNT       = new Solving(ModelPartitioner.COUNT.getSolving());

  public static final int     MAXCOUNT    = 0;
  public static final int     NOTIMEOUT   = -1;

  private Mapper              mapper      = new arf.model.util.Mapper();
  private ModelPartitioner    partitioner = null;

  private long                timeout     = NOTIMEOUT;
  private int                 count       = MAXCOUNT;
  private Solving             solving     = null;

  public PartitioningSolver(long timeout, int count, Solving solving) {
    this.timeout = timeout;
    this.count = count;
    this.solving = solving;
    this.partitioner = new ModelPartitioner(this.mapper, solving);
  }

  public void solve(ARFModel arfModel, ARFContext arfContext, ModelPartitionerListener listener) throws Throwable {
    this.partitioner.partition(arfModel, arfContext, this.count, listener);
  }

  public void sychronise(ModelPartitionerListener listener) throws Exception {
    listener.synchronise(this.timeout);
  }

  private void solve(ARFModel arfModel, ARFContext arfContext, Result result, ModelPartitionerListener listener) {
    try {
      solve(arfModel, arfContext, listener);
      sychronise(listener);
    }
    catch (Throwable e) {
      result.getLog().getStatistics().getErrors().add(e.getLocalizedMessage());
    }
  }

  public BooleanResult exist(ModelHandle model, InputHandle input) {
    ARFModel arfModel = this.mapper.mapModel(model.getModel());
    ARFContext arfContext = this.mapper.mapContext(model.getModel(), input != null ? input.getContext() : null);

    BooleanResult result = new BooleanResult();
    AllPMRConfigurationListener listener = new AllPMRConfigurationListener(this.partitioner, this.mapper, arfModel, arfContext);

    solve(arfModel, arfContext, result, listener);

    result.setAnswer(listener.hasSuccess());

    return result;
  }

  public NumericResult count(ModelHandle model, InputHandle input) {
    ARFModel arfModel = this.mapper.mapModel(model.getModel());
    ARFContext arfContext = this.mapper.mapContext(model.getModel(), input != null ? input.getContext() : null);

    NumericResult result = new NumericResult();
    CountPMRConfigurationListener listener = new CountPMRConfigurationListener(this.partitioner, this.mapper, arfModel, arfContext);

    solve(arfModel, arfContext, result, listener);

    result.setAnswer(listener.count());
    return result;
  }

  public ConfigurationResult solve(ModelHandle model, InputHandle input) {
    ARFModel arfModel = this.mapper.mapModel(model.getModel());
    ARFContext arfContext = this.mapper.mapContext(model.getModel(), input != null ? input.getContext() : null);

    ConfigurationResult result = new ConfigurationResult();
    if (this.solving == BEST) {
      BestPMRConfigurationListener listener = new BestPMRConfigurationListener(model.getModel(), this.partitioner, this.mapper, arfModel, arfContext);

      solve(arfModel, arfContext, result, listener);

      Resource resource = listener.best;
      if (resource != null) {
        result.getAnswer().add(resource);
      }
    }
    else if (this.solving == NEXT) {
      NextPMRConfigurationListener listener = new NextPMRConfigurationListener(model.getModel(), input != null ? input.getConfiguration() : null,
          this.partitioner, this.mapper, arfModel, arfContext);

      solve(arfModel, arfContext, result, listener);

      Resource resource = listener.next;
      if (resource != null) {
        result.getAnswer().add(resource);
      }
    }
    else {
      AllPMRConfigurationListener listener = new AllPMRConfigurationListener(this.partitioner, this.mapper, arfModel, arfContext);
      solve(arfModel, arfContext, result, listener);
      result.getAnswer().addAll(listener.all);
    }

    return result;
  }

}
