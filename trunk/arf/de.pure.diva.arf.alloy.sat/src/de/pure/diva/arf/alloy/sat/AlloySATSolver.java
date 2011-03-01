package de.pure.diva.arf.alloy.sat;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.Result;
import service.arf.reasoner.Solver;
import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.util.Mapper;
import arf.question.BooleanResult;
import arf.question.ConfigurationResult;
import arf.question.NumericResult;
import de.pure.diva.arf.alloy.sat.util.ARFModel2AlloyGenerator;
import de.pure.diva.arf.alloy.sat.util.AllAlloySolutionListener;
import de.pure.diva.arf.alloy.sat.util.AlloyConnector;
import de.pure.diva.arf.alloy.sat.util.AlloyConnectorListener;
import de.pure.diva.arf.alloy.sat.util.BestAlloySolutionListener;
import de.pure.diva.arf.alloy.sat.util.CountAlloySolutionListener;
import de.pure.diva.arf.alloy.sat.util.NextAlloySolutionListener;

public class AlloySATSolver implements Solver {
  private ARFModel2AlloyGenerator aGenerator = new ARFModel2AlloyGenerator();

  public static class Solving {
    private String solving = null;

    public Solving(String solving) {
      this.solving = solving;
    }

    public String getSolving() {
      return this.solving;
    }
  }

  public static final Solving BEST      = new Solving("best");
  public static final Solving NEXT      = new Solving("next");
  public static final Solving ALL       = new Solving("all");
  public static final Solving NO        = new Solving("no");

  public static final int     MAXCOUNT  = 0;
  public static final int     NOTIMEOUT = -1;

  private Mapper              mapper    = new arf.model.util.Mapper();
  public AlloyConnector       connector = new AlloyConnector();

  private long                timeout   = NOTIMEOUT;
  private int                 count     = MAXCOUNT;
  private Solving             solving   = null;

  public AlloySATSolver(long timeout, int count, Solving solving) {
    this.timeout = timeout;
    this.count = count;
    this.solving = solving;
  }

  public void solve(String system, String context, AlloyConnectorListener listener) throws Throwable {
    this.connector.solve(system, context, this.count, this.solving == NO, listener);
  }

  public void sychronise(AlloyConnectorListener listener) throws Exception {
    listener.synchronise(this.timeout);
  }

  private void solve(ARFModel arfModel, ARFContext arfContext, Result result, AlloyConnectorListener listener) {
    String system = aGenerator.generate(arfModel);
    String context = aGenerator.select(arfModel, arfContext);
    try {
      solve(system, context, listener);
      sychronise(listener);
    }
    catch (Throwable e) {
      result.getLog().getStatistics().getErrors().add(e.getLocalizedMessage());
    }
  }

  private void solve(ARFModel arfModel, ARFConfiguration arfConfiguration, Result result, AlloyConnectorListener listener) {
    String system = aGenerator.generate(arfModel);
    String context = aGenerator.select(arfModel, arfConfiguration);
    try {
      solve(system, context, listener);
      sychronise(listener);
    }
    catch (Throwable e) {
      result.getLog().getStatistics().getErrors().add(e.getLocalizedMessage());
    }
  }

  public BooleanResult check(ModelHandle model, InputHandle input) {
    ARFModel arfModel = this.mapper.mapModel(model.getModel());
    ARFContext arfContext = this.mapper.mapContext(model.getModel(), input != null ? input.getContext() : null);
    ARFConfiguration arfConfiguration = this.mapper.mapConfiguration(model.getModel(), input != null ? input.getContext() : null, input != null ? input
        .getConfiguration() : null);

    BooleanResult result = new BooleanResult();
    AllAlloySolutionListener listener = new AllAlloySolutionListener(this.connector, this.mapper, arfModel, arfContext);

    solve(arfModel, arfConfiguration, result, listener);

    result.setAnswer(listener.count() == 1);
    return result;
  }

  public BooleanResult exist(ModelHandle model, InputHandle input) {
    ARFModel arfModel = this.mapper.mapModel(model.getModel());
    ARFContext arfContext = this.mapper.mapContext(model.getModel(), input != null ? input.getContext() : null);

    BooleanResult result = new BooleanResult();
    AllAlloySolutionListener listener = new AllAlloySolutionListener(this.connector, this.mapper, arfModel, arfContext);

    solve(arfModel, arfContext, result, listener);

    result.setAnswer(listener.hasSuccess());
    return result;
  }

  public NumericResult count(ModelHandle model, InputHandle input) {
    ARFModel arfModel = this.mapper.mapModel(model.getModel());
    ARFContext arfContext = this.mapper.mapContext(model.getModel(), input != null ? input.getContext() : null);

    NumericResult result = new NumericResult();
    CountAlloySolutionListener listener = new CountAlloySolutionListener(this.connector, this.mapper, arfModel, arfContext);

    solve(arfModel, arfContext, result, listener);

    result.setAnswer(listener.count());
    return result;
  }

  public ConfigurationResult solve(ModelHandle model, InputHandle input) {
    ARFModel arfModel = this.mapper.mapModel(model.getModel());
    ARFContext arfContext = this.mapper.mapContext(model.getModel(), input != null ? input.getContext() : null);

    ConfigurationResult result = new ConfigurationResult();
    if (this.solving == BEST) {
      BestAlloySolutionListener listener = new BestAlloySolutionListener(model.getModel(), this.connector, this.mapper, arfModel, arfContext);

      solve(arfModel, arfContext, result, listener);

      Resource resource = listener.best;
      if (resource != null) {
        result.getAnswer().add(resource);
      }
    }
    else if (this.solving == NEXT) {
      NextAlloySolutionListener listener = new NextAlloySolutionListener(model.getModel(), input != null ? input.getConfiguration() : null, this.connector,
          this.mapper, arfModel, arfContext);

      solve(arfModel, arfContext, result, listener);

      Resource resource = listener.next;
      if (resource != null) {
        result.getAnswer().add(resource);
      }
    }
    else {
      AllAlloySolutionListener listener = new AllAlloySolutionListener(this.connector, this.mapper, arfModel, arfContext);
      solve(arfModel, arfContext, result, listener);
      result.getAnswer().addAll(listener.all);
    }
    return result;
  }

}
