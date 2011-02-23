package service.arf;

/**
 * The log interface provides access to a protocol and a statistics regarding a
 * reasoning and validation task.
 */
public interface Log {
  /**
   * An empty logging information object.
   */
  public final static class EMPTY implements Log {
    public static Log instance() {
      return new EMPTY();
    }

    public Protocol getProtocol() {
      return Protocol.EMPTY.instance();
    }

    public Statistics getStatistics() {
      return Statistics.EMPTY.instance();
    }
  }

  /**
   * Get the protocol information of a reasoning and validation task.
   * 
   * @return The protocol.
   */
  public Protocol getProtocol();

  /**
   * Get the statistics of a reasoning and validation task.
   * 
   * @return The statistics.
   */
  public Statistics getStatistics();
}
