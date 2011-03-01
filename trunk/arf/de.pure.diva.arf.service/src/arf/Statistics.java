package arf;

import java.util.ArrayList;
import java.util.List;

/**
 * The statistics class contains errors, warnings and information of a reasoning
 * and validation task.
 */
public class Statistics implements service.arf.Statistics {

  /**
   * List of errors of a reasoning and validation task.
   */
  protected List<String> errors   = new ArrayList<String>();
  /**
   * List of warnings of a reasoning and validation task.
   */
  protected List<String> warnings = new ArrayList<String>();
  /**
   * List of information of a reasoning and validation task.
   */
  protected List<String> infos    = new ArrayList<String>();

  public List<String> getErrors() {
    return this.errors;
  }

  public List<String> getInfos() {
    return this.infos;
  }

  public List<String> getWarnings() {
    return this.warnings;
  }
}
