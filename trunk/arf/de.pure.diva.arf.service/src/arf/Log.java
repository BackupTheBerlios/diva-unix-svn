package arf;

import service.arf.Protocol;
import service.arf.Statistics;

/**
 * The reasoning log class contains a protocol object and a statistics object
 * which have information about a reasoning and validation task.
 */
public class Log implements service.arf.Log {
  /**
   * Protocol information object for a reasoning and validation task.
   */
  protected Protocol   protocol   = new arf.Protocol();
  /**
   * Statistics object for a reasoning and validation task.
   */
  protected Statistics statistics = new arf.Statistics();

  public Protocol getProtocol() {
    return this.protocol;
  }

  public Statistics getStatistics() {
    return this.statistics;
  }

}
