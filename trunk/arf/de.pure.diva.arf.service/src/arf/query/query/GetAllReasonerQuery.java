package arf.query.query;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import service.arf.ARF;
import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.query.Information;
import service.arf.query.ReasonerQuery;
import service.arf.question.QuestionHandle;
import service.arf.reasoner.Reasoner;
import arf.query.ReasonerQueryService;
import arf.reasoner.ReasonerManager;

public final class GetAllReasonerQuery extends ReasonerQueryService implements ReasonerQuery {

  public Information ask(ModelHandle model, InputHandle input, QuestionHandle question) {
    arf.query.Information information = new arf.query.Information();
    if (arf.ARF.getContext() != null) {
      String clazz = Reasoner.class.getName();
      String filter = createFilter(model, input, question);
      ServiceReference[] refs = getServices(clazz, filter);
      for (ServiceReference ref : refs) {
        Object service = arf.ARF.getContext().getService(ref);
        if (service instanceof Reasoner) {
          Reasoner reasoner = (Reasoner) service;
          information.addReasoner(reasoner);
        }
      }
    }
    return information;
  }

  /**
   * Returns all registered services from framework.
   * 
   * @param clazz
   *          The class for service.
   * @param filter
   *          The filter, which is applied to the set properties of a service.
   * @return All service references.
   */
  private ServiceReference[] getServices(String clazz, String filter) {
    BundleContext context = arf.ARF.getContext();
    ServiceReference[] refs = null;
    if (context != null) {
      try {
        refs = context.getServiceReferences(clazz, filter);
      }
      catch (Exception e) {}
      for (int i = 0; refs != null && i < refs.length; ++i) {
        context.ungetService(refs[i]);
      }
    }
    return refs == null ? new ServiceReference[0] : refs;
  }

  private String getModelHandleInterfaceName(ModelHandle model) {
    String name = model.getClass().getName();
    if (model instanceof arf.ModelHandle) {
      name = arf.ModelHandle.class.getName();
    }
    return name;
  }

  private String getInputHandleInterfaceName(InputHandle input) {
    String name = input.getClass().getName();
    if (input instanceof arf.InputHandle) {
      name = arf.InputHandle.class.getName();
    }
    return name;
  }

  private String createFilter(ModelHandle model, InputHandle input, QuestionHandle question) {
    StringBuffer filter = new StringBuffer();
    filter.append("(&");
    filter.append("(");
    filter.append(ReasonerManager.DICTIONARY_ENTRY_REASONER);
    filter.append("=*)");
    if (model != null) {
      filter.append("(|");
      filter.append("(");
      filter.append(ReasonerManager.DICTIONARY_ENTRY_MODEL_TYPE);
      filter.append("=*)");
      filter.append("(");
      filter.append(model.getClass().getName());
      filter.append("=*)");
      filter.append("(");
      filter.append(getModelHandleInterfaceName(model));
      filter.append("=*)");
      filter.append(")");
    }
    if (input != null) {
      filter.append("(|");
      filter.append("(");
      filter.append(ReasonerManager.DICTIONARY_ENTRY_INPUT_TYPE);
      filter.append("=*)");
      filter.append("(");
      filter.append(input.getClass().getName());
      filter.append("=*)");
      filter.append("(");
      filter.append(getInputHandleInterfaceName(input));
      filter.append("=*)");
      filter.append(")");
    }
    if (question != null) {
      filter.append("(|");
      filter.append("(");
      filter.append(ReasonerManager.DICTIONARY_ENTRY_QUESTION);
      filter.append("=*)");
      filter.append("(");
      filter.append(question.getQuestion());
      filter.append("=*)");
      filter.append(")");
    }
    filter.append(")");
    return filter.toString();
  }

  public String getName() {
    return ARF.QUERIES.GET_ALL_REASONER;
  }

  public String getQuery() {
    return "Get all registered reasoner from framework.";
  }

}
