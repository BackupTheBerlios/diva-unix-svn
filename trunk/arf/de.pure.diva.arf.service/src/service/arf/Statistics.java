package service.arf;

import java.util.Collections;
import java.util.List;

/**
 * The statistics interface provides access to errors, warnings and information
 * of a reasoning and validation task.
 */
public interface Statistics {
  /**
   * An empty statistics object.
   */
  public final static class EMPTY implements Statistics {
    public static Statistics instance() {
      return new EMPTY();
    }

    public List<String> getErrors() {
      return Collections.emptyList();
    }

    public List<String> getInfos() {
      return Collections.emptyList();
    }

    public List<String> getWarnings() {
      return Collections.emptyList();
    }
  }

  /**
   * Get information about reasoning and validation task.
   * 
   * @return All information.
   */
  public List<String> getInfos();

  /**
   * Get warning of reasoning and validation task.
   * 
   * @return All warnings.
   */
  public List<String> getWarnings();

  /**
   * Get errors of reasoning and validation task.
   * 
   * @return All errors.
   */
  public List<String> getErrors();
}
