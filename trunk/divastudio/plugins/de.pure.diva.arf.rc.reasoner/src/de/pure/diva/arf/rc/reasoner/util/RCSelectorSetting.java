package de.pure.diva.arf.rc.reasoner.util;

import de.pure.diva.arf.alloy.sat.AlloySATSolver;

public class RCSelectorSetting {

  public static final int               INFINITE_STEPS                   = 0;

  public static final Boolean           BACKTRACE_TO_STEPONE             = Boolean.TRUE;
  public static final Boolean           BACKTRACE_AT_STEPMAX             = Boolean.FALSE;
  public static final Boolean           BACKTRACE_TO_MINUSONE            = null;

  public static final int               MAX_SOLVE_COUNT                  = AlloySATSolver.MAXCOUNT;
  public static final long              NO_SOLVE_TIMEOUT                 = AlloySATSolver.NOTIMEOUT;

  public static final int               DEFAULT_REUSE_CONFIGURATION_RATE = 33;
  public static final int               DEFAULT_SELECT_RATE              = 50;
  public static final int               DEFAULT_STEPS                    = 1;
  public static final Boolean           DEFAULT_BACKTRACE                = BACKTRACE_TO_MINUSONE;
  public static final int               DEFAULT_SOLVE_COUNT              = 1;
  public static final long              DEFAULT_SOLVE_TIMEOUT            = NO_SOLVE_TIMEOUT;

  protected int                         configurationrate                = DEFAULT_REUSE_CONFIGURATION_RATE;
  protected int                         selectrate                       = DEFAULT_SELECT_RATE;
  protected int                         steps                            = DEFAULT_STEPS;
  protected Boolean                     backtrace                        = DEFAULT_BACKTRACE;
  protected int                         solvecount                       = DEFAULT_SOLVE_COUNT;
  protected long                        solvetimeout                     = DEFAULT_SOLVE_TIMEOUT;

  public static final RCSelectorSetting DEFAULT                          = new RCSelectorSetting();

  private RCSelectorSetting() {}

  public RCSelectorSetting(int configurationrate, int selectrate, int steps, Boolean backtrace, int solvecount, long solvetimeout) {
    this.configurationrate = configurationrate;
    this.selectrate = selectrate;
    this.steps = steps;
    this.backtrace = backtrace;
    this.solvecount = solvecount;
    this.solvetimeout = solvetimeout;
  }
}
