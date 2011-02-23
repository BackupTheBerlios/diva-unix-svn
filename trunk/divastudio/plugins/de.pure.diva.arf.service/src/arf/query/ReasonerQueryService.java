package arf.query;

import service.arf.query.ReasonerQuery;

/**
 * This class is an abstract base class for reasoner-queries of the adaptation
 * reasoning framework. This class registers and unregisters the queries at the
 * QueryTrader.
 */
public abstract class ReasonerQueryService implements ReasonerQuery {

  /**
   * Called when this query is started so the framework can perform the
   * query-specific activities necessary to start this query.
   */
  public void start() {
    QueryTrader.getInstance().registerQuery(this);
  }

  /**
   * Called when this query is stopped so the framework can perform the
   * query-specific activities necessary to stop the query.
   */
  public void stop() {
    QueryTrader.getInstance().unregisterQuery(this);
  }

}
