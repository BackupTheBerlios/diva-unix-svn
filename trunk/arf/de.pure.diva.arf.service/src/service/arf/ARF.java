package service.arf;

import org.osgi.framework.BundleException;

import service.arf.query.Information;
import service.arf.query.ReasonerQueryHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.ReasonerHandle;

/**
 * The adaptation reasoning and validation framework to reason on DiVA problems.
 * The framework gets a model handle and optionally a input handle as input.
 * These are set to the framework before asking queries or questions.
 * 
 * A reasoner query can be ask to the framework to get information regarding
 * available reasoner from the framework. A question can be ask to reason on the
 * model handle and the input handle. Questions return results, which contains
 * answers and logging information.
 */
public interface ARF {
  /**
   * Constants for all names of questions which are fixed part of the framework.
   */
  public final static class QUESTIONS {
    public final static String UNKNOWN                        = null;
    public final static String ARE_THERE_VALID_CONFIGURATIONS = "ARE_THERE_VALID_CONFIGURATIONS";
    public final static String HOW_MANY_VALID_CONFIGURATIONS  = "HOW_MANY_VALID_CONFIGURATIONS";
    public final static String GET_ALL_VALID_CONFIGURATIONS   = "GET_ALL_VALID_CONFIGURATIONS";
    public final static String GET_ONE_VALID_CONFIGURATION    = "GET_ONE_VALID_CONFIGURATION";
    public final static String IS_VALID_CONFIGURATION         = "IS_VALID_CONFIGURATION";
    public final static String GET_NEXT_VALID_CONFIGURATION   = "GET_NEXT_VALID_CONFIGURATION";
    public final static String GET_BEST_VALID_CONFIGURATIONS  = "GET_BEST_VALID_CONFIGURATIONS";
  }

  /**
   * Constants for all names of queries which are fixed part of the framework.
   */
  public final static class QUERIES {
    public final static String UNKNOWN          = null;
    public final static String GET_ALL_REASONER = "GET_ALL_REASONER";
  }

  /**
   * Constants for all names of reasoner which are fixed part of the reasoner.
   */
  public final static class REASONER {
    public static final String UNKNOWN = null;
  }

  /**
   * Set a reasoner to the framework. This reasoner will be used for reasoning
   * and validation. If no reasoner is set the frame work automatically selects
   * a reasoner.
   * 
   * @param reasoner
   *          The reasoner handle or <b>null</b>.
   */
  public void setReasoner(ReasonerHandle reasoner);

  /**
   * Set a reasoning model to the framework. The reasoning model has to be set
   * before asking a question.
   * 
   * @param model
   *          The model handle.
   */
  public void setModel(ModelHandle model);

  /**
   * Set reasoning input data. This input data is optional and limits the
   * solution space.
   * 
   * @param input
   *          The input handle.
   */
  public void setInput(InputHandle input);

  /**
   * Asking the framework for the given question.
   * 
   * @param question
   *          The question handle.
   * @return The result containing answer of question and logging information.
   */
  public Result ask(QuestionHandle question);

  /**
   * Ask the framework for the given reasoner query.
   * 
   * @param query
   *          The reasoner query handle.
   * @param question
   *          The question handle to be answered by returned reasoner or
   *          <b>null</b>.
   * @return Information about available reasoner.
   */
  public Information ask(ReasonerQueryHandle query, QuestionHandle question);

  /**
   * Loads a new bundle, which contains a reasoner. Loading bundle means
   * installing the bundle and start it afterwards.
   * 
   * @param bundleLocation
   *          The bundle location URL (e.g. file://C:/bundle.jar).
   * @return The reasoner handle for loaded reasoner.
   * @throws BundleException
   */
  public ReasonerHandle loadReasoner(String bundleLocation) throws BundleException;

  /**
   * Unloads a bundle, which contains a reasoner. Unloading bundle means
   * stopping the bundle and uninstalling it afterwards.
   * 
   * @param reasoner
   *          The reasoner handle.
   * @throws BundleException
   */
  public void unloadReasoner(ReasonerHandle reasoner) throws BundleException;
}
