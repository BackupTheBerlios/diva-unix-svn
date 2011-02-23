package arf;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.query.Information;
import service.arf.query.ReasonerQuery;
import service.arf.query.ReasonerQueryHandle;
import service.arf.question.Question;
import service.arf.question.QuestionHandle;
import service.arf.question.Result;
import service.arf.reasoner.Reasoner;
import service.arf.reasoner.ReasonerHandle;
import arf.query.ReasonerQueryService;
import arf.query.query.GetAllReasonerQuery;
import arf.question.ConfigurationResult;
import arf.question.QuestionService;
import arf.question.URIResult;
import arf.question.question.AreThereConfigsQuestion;
import arf.question.question.GetAllConfigsQuestion;
import arf.question.question.GetBestConfigsQuestion;
import arf.question.question.GetNextConfigQuestion;
import arf.question.question.GetOneConfigQuestion;
import arf.question.question.HowManyConfigsQuestion;
import arf.question.question.IsConfigQuestion;
import arf.reasoner.ReasonerManager;

/**
 * The adaptation reasoning and validation framework to reason on DiVA problems.
 * The framework gets a work package-dependent model handle and optionally a
 * work package-dependent input handle as input. These are set to the framework
 * before asking queries or questions.
 * 
 * A reasoner query can be ask to the framework to get information regarding
 * available reasoner from the framework. A question can be ask to reason on the
 * work package-dependent model handle and the work package-dependent input
 * handle. Questions return results, which contains answers and logging
 * information.
 */
public class ARF implements service.arf.ARF {
  private final static class QUESTIONS {
    private final static QuestionService ARE_THERE_VALID_CONFIGURATIONS = new AreThereConfigsQuestion();
    private final static QuestionService HOW_MANY_VALID_CONFIGURATIONS  = new HowManyConfigsQuestion();
    private final static QuestionService GET_ALL_VALID_CONFIGURATIONS   = new GetAllConfigsQuestion();
    private final static QuestionService GET_BEST_VALID_CONFIGURATIONS  = new GetBestConfigsQuestion();
    private final static QuestionService GET_NEXT_VALID_CONFIGURATION   = new GetNextConfigQuestion();
    private final static QuestionService GET_ONE_VALID_CONFIGURATION    = new GetOneConfigQuestion();
    private final static QuestionService IS_VALID_CONFIGURATION         = new IsConfigQuestion();
  }

  private final static class QUERIES {
    private static ReasonerQueryService GET_ALL_REASONER = new GetAllReasonerQuery();
  }

  public static void init() {}

  static {
    QUERIES.GET_ALL_REASONER.start();
    QUESTIONS.ARE_THERE_VALID_CONFIGURATIONS.start();
    QUESTIONS.HOW_MANY_VALID_CONFIGURATIONS.start();
    QUESTIONS.GET_ALL_VALID_CONFIGURATIONS.start();
    QUESTIONS.GET_BEST_VALID_CONFIGURATIONS.start();
    QUESTIONS.GET_NEXT_VALID_CONFIGURATION.start();
    QUESTIONS.GET_ONE_VALID_CONFIGURATION.start();
    QUESTIONS.IS_VALID_CONFIGURATION.start();
  }

  private final class ARFServiceListener implements ServiceListener {
    private Reasoner reasoner = null;

    /*
     * (non-Javadoc)
     * 
     * @seeorg.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.
     * ServiceEvent)
     */
    public void serviceChanged(ServiceEvent event) {
      ServiceReference reference = event.getServiceReference();
      BundleContext context = reference.getBundle().getBundleContext();
      Object service = context.getService(reference);
      if (service instanceof Reasoner) {
        this.reasoner = (Reasoner) service;
      }
    }

    public Reasoner getReasoner() {
      return this.reasoner;
    }
  }

  private static BundleContext context  = null;
  private ReasonerHandle       reasoner = null;
  private ModelHandle          model    = null;
  private InputHandle          input    = null;

  /**
   * Construct an adaptation reasoning framework.
   */
  public ARF() {}

  /**
   * Set the bundle context of the adaption reasoning framework.
   * 
   * @param context
   *          The bundle context.
   */
  public static void setContext(BundleContext context) {
    ARF.context = context;
  }

  /**
   * Get the bundle context of the adaption reasoning framework.
   * 
   * @return context The bundle context.
   */
  public static BundleContext getContext() {
    if (ARF.context == null) {
      ARF.setContext(Activator.getDefault().getContext());
    }
    return ARF.context;
  }

  /**
   * Sets a reasoner to the framework. This reasoner will be used for reasoning
   * and validation. If no reasoner is set the frame work automatically selects
   * a reasoner.
   * 
   * @param reasoner
   *          The reasoner handle or <b>null</b>.
   */
  public void setReasoner(ReasonerHandle reasoner) {
    this.reasoner = reasoner;
  }

  /**
   * Sets a reasoning model to the framework. The reasoning model has to be set
   * before asking a question.
   * 
   * @param model
   *          The model handle.
   */
  public void setModel(ModelHandle model) {
    this.model = model;
  }

  /**
   * Sets reasoning input data. This input data is optional and limits the
   * solution space.
   * 
   * @param input
   *          The input handle.
   */
  public void setInput(InputHandle input) {
    this.input = input;
  }

  /**
   * Called when asking the framework for the given question.
   * 
   * @param question
   *          The question handle.
   * @return The result containing answer of question and logging information.
   */
  public Result ask(QuestionHandle question) {
    Question q = arf.question.QuestionTrader.getInstance().getQuestion(question.getQuestion());
    ReasonerHandle reasoner = this.reasoner;
    if (reasoner == null) {
      reasoner = selectReasoner(question);
    }
    else {
      Reasoner r = ReasonerManager.getInstance().getReasoner(reasoner.getReasoner());
      if (r.getSupportedQuestions().contains(question.getQuestion()) == false) {
        reasoner = new arf.reasoner.ReasonerHandle(service.arf.ARF.REASONER.UNKNOWN);
      }
    }
    Result result = q.ask(reasoner, this.model, this.input, question);
    if (result instanceof ConfigurationResult) {
      // this is not the best way to decide getting a URIResult
      // but we need a loader only if we have an URI as input
      if (this.input instanceof arf.InputHandle && ((arf.InputHandle) this.input).getLoader() != null) {
        result = new URIResult((ConfigurationResult) result);
      }
    }
    return result;
  }

  private ReasonerHandle selectReasoner(QuestionHandle question) {
    Information information = ask(new arf.query.ReasonerQueryHandle(service.arf.ARF.QUERIES.GET_ALL_REASONER), question);
    ReasonerHandle reasoner = new arf.reasoner.ReasonerHandle(service.arf.ARF.REASONER.UNKNOWN);
    if (information.getReasoner().size() > 0) {
      reasoner = information.getReasoner().get(0);
    }
    return reasoner;
  }

  /**
   * Called when asking the framework for the given reasoner query.
   * 
   * @param query
   *          The reasoner query handle.
   * @param question
   *          The question handle to be answered by returned reasoner or
   *          <b>null</b>.
   * @return Information about availbale reasoner.
   */
  public Information ask(ReasonerQueryHandle query, QuestionHandle question) {
    ReasonerQuery q = arf.query.QueryTrader.getInstance().getQuery(query.getQuery());
    Information information = q.ask(this.model, this.input, question);
    if (information == null) {
      information = Information.EMPTY.instance();
    }
    return information;
  }

  /**
   * Loads a new bundle, which contains a reasoner. Loading bundle means
   * installing the bundle and start it afterwards. Before loading the context
   * has to be set.
   * 
   * @param bundleLocation
   *          The bundle location URL (e.g. file://C:/bundle.jar).
   * @return The reasoner handle for loaded reasoner.
   * @throws BundleException
   */
  public ReasonerHandle loadReasoner(String bundleLocation) throws BundleException {
    String name = service.arf.ARF.REASONER.UNKNOWN;
    BundleContext context = getContext();
    if (context != null) {
      ARFServiceListener listener = new ARFServiceListener();
      context.addServiceListener(listener);
      try {
        Bundle bundle = context.installBundle(bundleLocation);
        bundle.start();
      }
      finally {
        context.removeServiceListener(listener);
      }
      Reasoner reasoner = listener.getReasoner();
      if (reasoner != null) {
        name = reasoner.getName();
      }
    }
    return new arf.reasoner.ReasonerHandle(name);
  }

  /**
   * Unloads a bundle, which contains a reasoner. Unloading bundle means
   * stopping the bundle and uninstalling it afterwards. Before unloading the
   * context has to be set.
   * 
   * @param reasoner
   *          The reasoner handle.
   * @throws BundleException
   */
  public void unloadReasoner(ReasonerHandle reasoner) throws BundleException {
    Long bundleID = ReasonerManager.getInstance().getBundleID(reasoner);
    if (bundleID != null) {
      BundleContext context = getContext();
      if (context != null) {
        Bundle bundle = context.getBundle(bundleID.longValue());
        if (bundle != null) {
          bundle.stop();
          bundle.uninstall();
        }
      }
    }
  }
}
