package arf.model.util.mapper;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.featuremodel.Feature;
import org.eclipse.featuremodel.Group;
import org.eclipse.variantmodel.SelectionState;

import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;
import diva.BoolVariableValue;
import diva.BooleanVariable;
import diva.Context;
import diva.DivaFactory;
import diva.EnumLiteral;
import diva.EnumVariable;
import diva.EnumVariableValue;
import diva.Scenario;
import diva.Term;
import diva.VariabilityModel;
import diva.Variable;
import diva.VariableValue;
import diva.VariantExpression;

public class ContextModel2ARFContext {
  /**
   * Construct a new converter.
   */
  public ContextModel2ARFContext() {}

  private diva_context.ContextElement getContextElement(diva_context.Context context, Variable variable) {
    String id = variable.getId();
    return getContextElement(context, id);
  }

  private diva_context.ContextElement getContextElement(diva_context.Context context, String id) {
    diva_context.ContextElement element = null;
    EList<diva_context.ContextElement> elements = context.getElement();
    for (diva_context.ContextElement contextElement : elements) {
      if (contextElement.getName().equals(id) == true) {
        element = contextElement;
        break;
      }
    }
    return element;
  }

  private EnumLiteral getEnumLiteral(EnumVariable variable, diva_context.ContextElement element) {
    EnumLiteral literal = null;
    String value = element.getCurrentValue();
    EList<EnumLiteral> literals = variable.getLiteral();
    for (EnumLiteral enumLiteral : literals) {
      if (enumLiteral.getId().equals(value) == true) {
        literal = enumLiteral;
        break;
      }
    }
    return literal;
  }

  /**
   * Convert the given context to an internal ARF context.
   * 
   * @param resourceContext
   *          The context model resource.
   * @param resourceModel
   *          The DiVA variability model resource.
   * @param arfModel
   *          The internal ARF model.
   * @return The ARF context.
   */
  public ARFContext convert(Resource resourceContext, Resource resourceModel, ARFModel arfModel) {
    Context context = null;
    EObject objectModel = resourceModel.getContents().get(0);
    EObject objectContext = resourceContext.getContents().get(0);
    if (objectModel instanceof VariabilityModel) {
      VariabilityModel model = (VariabilityModel) objectModel;
      if (objectContext instanceof diva_context.Context) {
        diva_context.Context contextModel = (diva_context.Context) objectContext;
        Scenario scenario = ARFModeling.createReasoningScenario(model);
        context = DivaFactory.eINSTANCE.createContext();
        EList<Variable> variables = model.getContext();
        for (Variable variable : variables) {
          VariableValue value = null;
          diva_context.ContextElement element = getContextElement(contextModel, variable);
          if (element != null) {
            if (variable instanceof BooleanVariable) {
              value = DivaFactory.eINSTANCE.createBoolVariableValue();
              ((BoolVariableValue) value).setBool(Boolean.parseBoolean(element.getCurrentValue()));
            }
            else {
              value = DivaFactory.eINSTANCE.createEnumVariableValue();
              ((EnumVariableValue) value).setLiteral(getEnumLiteral((EnumVariable) variable, element));
            }
          }
          else {
            if (variable instanceof BooleanVariable) {
              value = DivaFactory.eINSTANCE.createBoolVariableValue();
              ((BoolVariableValue) value).setBool(false);
            }
            else {
              value = DivaFactory.eINSTANCE.createEnumVariableValue();
              ((EnumVariableValue) value).setLiteral(((EnumVariable) variable).getLiteral().get(0));
            }
          }
          value.setVariable(variable);
          context.getVariable().add(value);
        }
        diva_context.ContextElement element = getContextElement(contextModel, ARFModelConstants.TEMPORARY_REASONING_ORACLE);
        if (element != null) {
          String expr = element.getCurrentValue();
          if (expr.startsWith(ARFModelConstants.REASONING_ORACLE_START) == true) {
            expr = expr.replaceFirst(ARFModelConstants.REASONING_ORACLE_START, "");
            VariantExpression oracle = DivaFactory.eINSTANCE.createVariantExpression();
            oracle.setText(expr);
            context.setOracle(oracle);
          }
        }
        scenario.getContext().add(context);
      }
      else if (objectContext instanceof VariabilityModel) {
        model = (VariabilityModel) objectContext;
        Scenario scenario = ARFModeling.getReasoningScenario(model);
        if (scenario != null) {
          context = scenario.getContext().get(0);
        }
      }
    }
    return convert(context, arfModel);
  }

  /**
   * Convert the given context to an internal ARF context.
   * 
   * @param context
   *          The context.
   * @param arfModel
   *          The internal ARF model.
   * @return The ARF context.
   */
  public ARFContext convert(Context context, ARFModel arfModel) {
    ARFContext arfContext = new ARFContext();
    ARFModeling.setModelName(arfContext, ARFModelConstants.CONTEXT_NAME);
    if (context != null) {
      // covert oracles of contexts
      convertOracles(context, arfModel, arfModel.getRoot());
      // add the context variables as selections to the VDM
      EList<VariableValue> list = context.getVariable();
      for (VariableValue v : list) {

        String uname = null;
        boolean is_selected = false;

        if (v instanceof BoolVariableValue && ((BoolVariableValue) v).getVariable() != null) {
          // boolean variable, select if value is true
          BoolVariableValue bv = (BoolVariableValue) v;
          uname = ARFModelConstants.VARIABLE_PREFIX + v.getVariable().getId();
          is_selected = bv.isBool();
        }
        else if (v instanceof EnumVariableValue && ((EnumVariableValue) v).getVariable() != null) {
          // enumeration variable, select if value is true
          EnumVariableValue ev = (EnumVariableValue) v;
          EnumVariable variable = (EnumVariable) ev.getVariable();
          EnumLiteral literal = ev.getLiteral();
          if (literal != null) {
            uname = ARFModelConstants.VARIABLE_PREFIX + variable.getId() + "_" + literal.getId();
            is_selected = true;
          }
          for (EnumLiteral tmp : variable.getLiteral()) {
            if (tmp != literal) {
              excludeFeature(arfContext, ARFModelConstants.VARIABLE_PREFIX + variable.getId() + "_" + tmp.getId(), arfModel);
            }
          }
        }

        if (uname != null) {
          if (is_selected) {
            // select the corresponding feature
            selectFeature(arfContext, uname, arfModel);
          }
          else {
            // exclude the corresponding feature
            excludeFeature(arfContext, uname, arfModel);
          }
        }
      }
      // set resource modified
      context.eResource().setModified(true);
    }
    else {
      ARFModeling.setModelName(arfContext, "NoContext");
    }
    return arfContext;
  }

  /**
   * Convert context oracles. This maps to optional features.
   * 
   * @param c
   *          The context.
   * @param root
   *          The ARF model root feature.
   */
  protected void convertOracles(Context c, ARFModel arfModel, Feature root) {
    // remove a group feature
    Feature group = null;
    for (Group children : root.getChildren()) {
      for (Feature child : children.getFeatures()) {
        if (ARFModelConstants.ORACLES_GROUP_UNAME.equals(child.getName()) == true) {
          group = child;
          break;
        }
        if (group != null) {
          break;
        }
      }
    }
    if (group != null) {
      ARFModeling.remChild(root, group);
    }
    // add a group feature
    group = ARFModeling.createFeature(ARFModelConstants.ORACLES_GROUP_UNAME, ARFModelConstants.ORACLES_GROUP_NAME, ARFModelConstants.ORACLES_GROUP_TYPE);
    ARFModeling.addChild(root, group, ARFModelConstants.MANDATORY);

    if (c.getOracle() != null) {
      Term term = c.getOracle().getTerm();
      String text = c.getOracle().getText();
      if (term != null || (text != null && "".equals(text) == false)) {
        Feature oracleGroup = ARFModeling.createFeature(ARFModelConstants.ORACLE_PREFIX + ARFModelConstants.GROUP_PREFIX + ARFModeling.getId(c),
            ARFModelConstants.GROUP_NAME_PREFIX + ARFModeling.getName(c), ARFModelConstants.ORACLE_ELEMENT_TYPE);
        ARFModeling.addChild(group, oracleGroup, ARFModelConstants.MANDATORY);

        String expr = null;
        if (term != null) {
          expr = ARFModeling.convertTerm(term);
        }
        else {
          expr = text;
        }

        Feature oracle = ARFModeling.createFeature(ARFModelConstants.ORACLE_PREFIX + ARFModeling.getId(c), ARFModeling.getName(c),
            ARFModelConstants.ORACLE_ELEMENT_TYPE);
        ARFModeling.addChild(oracleGroup, oracle, ARFModelConstants.ALTERNATIVE);
        {
          String content = expr + " implies " + oracle.getName();
          // add constraint with name "Context"
          ARFModeling.addConstraint(arfModel, ARFModelConstants.ORACLE_CONSTRAINT_NAME, content);
        }
        Feature oracleNot = ARFModeling.createFeature(ARFModelConstants.ORACLE_PREFIX + ARFModelConstants.NOT_PREFIX + ARFModeling.getId(c),
            ARFModelConstants.NOT_NAME_PREFIX + ARFModeling.getName(c), ARFModelConstants.ORACLE_ELEMENT_TYPE);
        ARFModeling.addChild(oracleGroup, oracleNot, ARFModelConstants.ALTERNATIVE);
        {
          String content = "not(" + expr + ") implies " + oracleNot.getName();
          // add constraint with name "Context"
          ARFModeling.addConstraint(arfModel, ARFModelConstants.ORACLE_CONSTRAINT_NAME, content);
        }
      }
    }
  }

  /**
   * Select the feature with the given name in the ARF context.
   * 
   * @param arfContext
   *          The ARF context.
   * @param uname
   *          The unique name of the feature to select.
   * @param arfModel
   *          The ARF model.
   */
  private void selectFeature(ARFContext arfContext, String uname, ARFModel arfModel) {
    addSelection(arfContext, uname, arfModel, SelectionState.SELECTED);
  }

  /**
   * Exclude the feature with the given name in the ARF context.
   * 
   * @param arfContext
   *          The ARF context.
   * @param uname
   *          The unique name of the feature to select.
   * @param arfModel
   *          The ARF model.
   */
  private void excludeFeature(ARFContext arfContext, String uname, ARFModel arfModel) {
    addSelection(arfContext, uname, arfModel, SelectionState.EXCLUDED);
  }

  /**
   * Add a selection to the ARF context for the feature with the given name.
   * 
   * @param arfContext
   *          The ARF context.
   * @param uname
   *          The unique name of the feature to select.
   * @param arfModel
   *          The ARF model.
   * @param state
   *          The selection state.
   */
  private void addSelection(ARFContext arfContext, String uname, ARFModel arfModel, SelectionState state) {
    ARFModeling.addSelection(arfContext, uname, arfModel, state);
  }

}
