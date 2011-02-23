package service.arf.query;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionHandle;

/**
 * A reasoner query interface represents a query, which is used to collect
 * information about the framework regarding available reasoner.
 */
public interface ReasonerQuery {
  /**
   * Returns the name of the reasoner query.
   * 
   * @return The name.
   */
  public String getName();

  /**
   * Returns the query.
   * 
   * @return The query.
   */
  public String getQuery();

  /**
   * Called when the framework is asked for information.
   * 
   * @param model
   *          The model handle.
   * @param input
   *          The input handle.
   * @param question
   *          The question handle.
   * @return The information about the framework.
   */
  public Information ask(ModelHandle model, InputHandle input, QuestionHandle question);

}
