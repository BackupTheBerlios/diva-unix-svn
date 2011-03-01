package arf.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;
import diva.BoolVariableValue;
import diva.BooleanVariable;
import diva.Configuration;
import diva.Context;
import diva.EnumVariableValue;
import diva.Priority;
import diva.Scenario;
import diva.Term;
import diva.VariabilityModel;
import diva.VariableValue;

public class Creator {

  /**
   * Separate all contexts and get a list of created context model resources for
   * the given DiVA variability model resource.
   * 
   * @param resource
   *          The DiVA variability model resource.
   * @return The list of all context model resources.
   */
  public Map<Resource, EObject> createContexts(Resource resource) {
    Map<Resource, EObject> contextResources = new HashMap<Resource, EObject>();
    for (EObject o : resource.getContents()) {
      if (o instanceof VariabilityModel) {
        VariabilityModel model = (VariabilityModel) o;
        EList<Scenario> scenarios = model.getSimulation().getScenario();
        for (Scenario scenario : scenarios) {
          EList<Context> contexts = scenario.getContext();
          for (Context context : contexts) {
            diva_context.Context contextModel = diva_context.Diva_contextFactory.eINSTANCE.createContext();
            EList<VariableValue> values = context.getVariable();
            for (VariableValue value : values) {
              String currentValue = null;
              if (value.getVariable() instanceof BooleanVariable) {
                if (((BoolVariableValue) value).isBool() == true) {
                  currentValue = "true";
                }
              }
              else {
                if (((EnumVariableValue) value).getLiteral() != null) {
                  currentValue = ((EnumVariableValue) value).getLiteral().getId();
                }
              }
              if (currentValue != null) {
                diva_context.ContextElement element = diva_context.Diva_contextFactory.eINSTANCE.createContextElement();
                element.setName(value.getVariable().getId());
                element.setCurrentValue(currentValue);
                contextModel.getElement().add(element);
              }
            }
            if (context.getOracle() != null) {
              Term term = context.getOracle().getTerm();
              if (term != null) {
                String expr = ARFModeling.convertTerm(term);
                diva_context.ContextElement element = diva_context.Diva_contextFactory.eINSTANCE.createContextElement();
                element.setName(ARFModelConstants.TEMPORARY_REASONING_ORACLE);
                element.setCurrentValue(ARFModelConstants.REASONING_ORACLE_START + expr);
                contextModel.getElement().add(element);
              }
            }
            // Create a resource for this file.
            ResourceSet resourceSet = new ResourceSetImpl();
            Resource contextResource = resourceSet.createResource(URI.createURI(resource.getURI().toString() + "_context.xmi"));
            // Add the initial model object to the contents.
            contextResource.getContents().add(contextModel);
            contextResources.put(contextResource, context);
          }
        }
      }
    }
    return contextResources;
  }

  /**
   * Correct the DiVA variability model.
   * 
   * @param resource
   *          The DiVA variability model resource.
   * @param object
   *          The original context of the DiVA variability model.
   */
  public void mergeFirstContext(Resource resource, EObject object) {
    Context original = (Context) object;
    if (resource.getContents().isEmpty() == false) {
      for (EObject o : resource.getContents()) {
        if (o instanceof VariabilityModel) {
          VariabilityModel model = (VariabilityModel) o;
          Scenario scenario = ARFModeling.getReasoningScenario(model);
          if (scenario != null) {
            Context context = scenario.getContext().isEmpty() == false ? scenario.getContext().get(0) : null;
            if (context != null && original != null) {
              original.getConfiguration().clear();
              EList<Configuration> configurations = context.getConfiguration();
              while (configurations.isEmpty() == false) {
                Configuration configuration = configurations.remove(0);
                original.getConfiguration().add(configuration);
              }
              context.getConfiguration().clear();
              original.getPriority().clear();
              EList<Priority> priorities = context.getPriority();
              while (priorities.isEmpty() == false) {
                Priority priority = priorities.remove(0);
                original.getPriority().add(priority);
              }
              context.getPriority().clear();
            }
          }
          ARFModeling.removeReasoningScenario(model);
        }
      }
    }
  }

}
