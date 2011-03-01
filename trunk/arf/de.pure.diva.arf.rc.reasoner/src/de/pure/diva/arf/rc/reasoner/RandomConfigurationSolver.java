package de.pure.diva.arf.rc.reasoner;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.Result;
import service.arf.reasoner.Solver;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.util.Mapper;
import arf.question.BooleanResult;
import arf.question.ConfigurationResult;
import de.pure.diva.arf.rc.reasoner.util.AllRCRConfigurationListener;
import de.pure.diva.arf.rc.reasoner.util.BestRCRConfigurationListener;
import de.pure.diva.arf.rc.reasoner.util.RCSelector;
import de.pure.diva.arf.rc.reasoner.util.RCSelectorListener;
import de.pure.diva.arf.rc.reasoner.util.RCSelectorSetting;

public class RandomConfigurationSolver implements Solver {
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

  public static final int     MAXCOUNT  = -1;
  public static final int     NOTIMEOUT = -1;

  private RCSelector          selector  = null;

  private long                timeout   = NOTIMEOUT;
  private int                 count     = MAXCOUNT;
  private Solving             solving   = null;

  private Mapper              mapper    = new arf.model.util.Mapper();

  public RandomConfigurationSolver(long timeout, int count, Solving solving, boolean check) {
    RCSelectorSetting setting = new RCSelectorSetting(RCSelectorSetting.DEFAULT_REUSE_CONFIGURATION_RATE, RCSelectorSetting.DEFAULT_SELECT_RATE,
        RCSelectorSetting.DEFAULT_STEPS, RCSelectorSetting.DEFAULT_BACKTRACE, check == false ? count : RCSelectorSetting.DEFAULT_SOLVE_COUNT, timeout);
    this.selector = new RCSelector(setting);
    this.timeout = timeout;
    this.count = count;
    this.solving = solving;
  }

  public void solve(ARFModel arfModel, ARFContext arfContext, RCSelectorListener listener) throws Throwable {
    this.selector.select(arfModel, arfContext, this.count, listener);
  }

  public void sychronise(RCSelectorListener listener) throws Exception {
    listener.synchronise(this.timeout);
  }

  private void solve(ARFModel arfModel, ARFContext arfContext, Result result, RCSelectorListener listener) {
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
    AllRCRConfigurationListener listener = new AllRCRConfigurationListener(this.selector, this.mapper, arfModel, arfContext);

    solve(arfModel, arfContext, result, listener);

    result.setAnswer(listener.all.isEmpty() == false);

    return result;
  }

  public ConfigurationResult solve(ModelHandle model, InputHandle input) {
    ARFModel arfModel = this.mapper.mapModel(model.getModel());
    ARFContext arfContext = this.mapper.mapContext(model.getModel(), input != null ? input.getContext() : null);

    ConfigurationResult result = new ConfigurationResult();
    if (this.solving == BEST) {
      BestRCRConfigurationListener listener = new BestRCRConfigurationListener(model.getModel(), this.selector, this.mapper, arfModel, arfContext);
      solve(arfModel, arfContext, result, listener);
      result.getAnswer().add(listener.best);
    }
    else {
      AllRCRConfigurationListener listener = new AllRCRConfigurationListener(this.selector, this.mapper, arfModel, arfContext);
      solve(arfModel, arfContext, result, listener);
      result.getAnswer().addAll(listener.all);
    }

    return result;
  }
}
