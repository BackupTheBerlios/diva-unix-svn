package arf.query;

import java.util.HashMap;
import java.util.Map;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.query.Information;
import service.arf.query.ReasonerQuery;
import service.arf.question.QuestionHandle;

/**
 * The query trader manages all queries of the framework.
 */
public class QueryTrader {
  private static final class UNKNOWN implements ReasonerQuery {
    private static ReasonerQuery instance() {
      return new UNKNOWN();
    }

    public Information ask(ModelHandle model, InputHandle input, QuestionHandle question) {
      return Information.EMPTY.instance();
    }

    public String getName() {
      return ARF.QUERIES.UNKNOWN;
    }

    public String getQuery() {
      return "";
    }
  }

  private Map<String, ReasonerQuery> queries  = new HashMap<String, ReasonerQuery>();
  private static QueryTrader         instance = null;

  /**
   * Returns an instance of QueryTrader class.
   * 
   * @return The singleton instance of this class.
   */
  public static QueryTrader getInstance() {
    if (instance == null) {
      instance = new QueryTrader();
    }
    return instance;
  }

  private QueryTrader() {}

  /**
   * Returns a query for the given name.
   * 
   * @param name
   *          The name of a query.
   * @return The query. If no query was registered an UNKNOWN-query will be
   *         returned.
   */
  public ReasonerQuery getQuery(String name) {
    ReasonerQuery query = this.queries.get(name);
    if (query == null) {
      query = UNKNOWN.instance();
    }
    return query;
  }

  /**
   * Called when a query is stared and available by the framework.
   * 
   * @param query
   *          The query.
   */
  public void registerQuery(ReasonerQuery query) {
    this.queries.put(query.getName(), query);
  }

  /**
   * Called when a query is stopped and not available anymore by the framework.
   * 
   * @param query
   *          The query.
   */
  public void unregisterQuery(ReasonerQuery query) {
    this.queries.remove(query.getName());
  }
}
