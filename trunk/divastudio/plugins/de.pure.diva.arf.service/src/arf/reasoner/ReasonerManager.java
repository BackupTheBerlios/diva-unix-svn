package arf.reasoner;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.BundleContext;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.Reasoner;
import service.arf.reasoner.ReasonerAttributes;
import service.arf.reasoner.ReasonerHandle;
import arf.question.BooleanResult;
import arf.question.ConfigurationResult;
import arf.question.UnknownResult;

/**
 * The reasoner manager manages all reasoner of the framework.
 */
public class ReasonerManager {

  private static final class UNKNOWN implements Reasoner {
    private static Reasoner instance() {
      return new UNKNOWN();
    }

    public String getName() {
      return ARF.REASONER.UNKNOWN;
    }

    public ReasonerAttributes getAttributes() {
      return new ReasonerAttributes();
    }

    public List<String> getSupportedHandles() {
      return Collections.emptyList();
    }

    public List<String> getSupportedQuestions() {
      return Collections.emptyList();
    }

    public Boolean optimising() {
      return Boolean.FALSE;
    }

    public Result reason(ModelHandle model, InputHandle input, QuestionHandle question) {
      Result result = new UnknownResult();
      if (question.getParameters().LOGGING == true) {
        StringBuffer msg = new StringBuffer();
        msg.append("Reasoning on problem '");
        msg.append(question.getQuestion());
        msg.append("' can not be performed.\n\n");
        msg.append("If a reasoner was set to the ARF, it was not registered to reason on that problem.\n");
        msg.append("If no reasoner was set to the ARF, the ARF did not find any reasoner to reason on that problem.");
        result.getLog().getStatistics().getErrors().add(msg.toString());
      }
      return result;
    }

    public Boolean solving() {
      return Boolean.FALSE;
    }

    public Boolean validating() {
      return Boolean.FALSE;
    }

    public ConfigurationResult optimise(ModelHandle model, InputHandle input) {
      return new ConfigurationResult();
    }

    public ConfigurationResult solve(ModelHandle model, InputHandle input) {
      return new ConfigurationResult();
    }

    public BooleanResult validate(ModelHandle model, InputHandle input) {
      return new BooleanResult();
    }

  }

  /**
   * Constant which defines the dictionary entry for a reasoner.
   */
  public static String           DICTIONARY_ENTRY_REASONER   = "Reasoner";
  /**
   * Constant which defines the dictionary entry for all model types.
   */
  public static String           DICTIONARY_ENTRY_MODEL_TYPE = "ModelType";
  /**
   * Constant which defines the dictionary entry for all input types.
   */
  public static String           DICTIONARY_ENTRY_INPUT_TYPE = "InputType";
  /**
   * Constant which defines the dictionary entry for all questions.
   */
  public static String           DICTIONARY_ENTRY_QUESTION   = "Question";

  private Map<String, Long>      bundles                     = new HashMap<String, Long>();
  private Map<String, Reasoner>  reasoner                    = new HashMap<String, Reasoner>();

  private static ReasonerManager instance                    = null;

  /**
   * Returns an instance of ReasonerManager class.
   * 
   * @return The singleton instance of this class.
   */
  public static ReasonerManager getInstance() {
    if (instance == null) {
      instance = new ReasonerManager();
    }
    return instance;
  }

  private ReasonerManager() {}

  /**
   * Registers the reasoner as service to the framework.
   * 
   * @param context
   *          The bundle context of the reasoner.
   * @param reasoner
   *          The reasoner.
   */
  public void registerReasoner(BundleContext context, Reasoner reasoner) {
    Properties props = new Properties();
    props.put(DICTIONARY_ENTRY_REASONER, reasoner.getName());
    List<String> questions = reasoner.getSupportedQuestions();
    for (String question : questions) {
      props.put(question, reasoner.getName());
    }
    List<String> clazzes = reasoner.getSupportedHandles();
    for (String clazz : clazzes) {
      props.put(clazz, reasoner.getName());
    }
    context.registerService(Reasoner.class.getName(), reasoner, props);
    add(context, reasoner);
  }

  /**
   * Called when a new reasoner is created to add it to the manager.
   * 
   * @param reasoner
   *          The reasoner.
   */
  private void add(BundleContext context, Reasoner reasoner) {
    if (reasoner != null) {
      if (context != null) {
        this.bundles.put(reasoner.getName(), context.getBundle().getBundleId());
      }
      else {
        this.bundles.remove(reasoner.getName());
      }
      this.reasoner.put(reasoner.getName(), reasoner);
    }
  }

  /**
   * Returns a reasoner for the given name.
   * 
   * @param name
   *          The name of a reasoner.
   * @return The reasoner. If no reasoner was registered an UNKNOWN-reasoner
   *         will be returned.
   */
  public Reasoner getReasoner(String name) {
    Reasoner reasoner = this.reasoner.get(name);
    if (reasoner == null) {
      reasoner = ReasonerManager.UNKNOWN.instance();
    }
    return reasoner;
  }

  /**
   * Returns the appropriate bundle ID for a handle.
   * 
   * @param reasoner
   *          The reasoner handle.
   * @return The bundle ID or <b>null</b>.
   */
  public Long getBundleID(ReasonerHandle reasoner) {
    Long l = null;
    if (reasoner != null) {
      l = this.bundles.get(reasoner.getReasoner());
    }
    return l;
  }

  /**
   * Returns all managed bundle identifier.
   * 
   * @return All bundle IDs.
   */
  public Long[] getBundleIds() {
    Set<Long> bundleIDs = new HashSet<Long>();
    bundleIDs.addAll(this.bundles.values());
    return bundleIDs.toArray(new Long[bundleIDs.size()]);
  }

  /**
   * Called when a reasoner is destroyed to remove it from the manager.
   * 
   * @param reasoner
   *          The reasoner.
   */
  public void unregisterReasoner(Reasoner reasoner) {
    if (reasoner != null) {
      this.bundles.remove(reasoner.getName());
      this.reasoner.remove(reasoner.getName());
    }
  }
}
