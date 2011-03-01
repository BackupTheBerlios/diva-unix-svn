package arf.model.util.mapper;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.featuremodel.Feature;

import arf.model.ARFModel;
import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;
import diva.BooleanVariable;
import diva.Dimension;
import diva.EnumLiteral;
import diva.EnumVariable;
import diva.PriorityRule;
import diva.PropertyPriority;
import diva.PropertyValue;
import diva.Rule;
import diva.VariabilityModel;
import diva.Variable;
import diva.Variant;

public class VariabilityModel2ARFModel {

  /**
   * Construct a new converter.
   */
  public VariabilityModel2ARFModel() {}

  /**
   * Creates an internal ARF model.
   * 
   * @param name
   *          The model name.
   * @return The internal ARF model.
   */
  protected ARFModel createARFModel(String name) {
    ARFModel arfModel = new ARFModel();
    ARFModeling.setModelName(arfModel, name);
    return arfModel;
  }

  /**
   * Convert the DiVA variability model to an internal ARF model.
   * 
   * @param resource
   *          The DiVA variability model resource.
   * @return The internal ARF model.
   */
  public ARFModel convert(Resource resource) {
    VariabilityModel model = null;
    EObject object = resource.getContents().get(0);
    if (object instanceof VariabilityModel) {
      model = (VariabilityModel) object;
    }
    return convert(model);
  }

  /**
   * Convert the DiVA variability model to an internal ARF model.
   * 
   * @param model
   *          The DiVA variability model.
   * @return The internal ARF model.
   */
  public ARFModel convert(VariabilityModel model) {
    String name = ARFModelConstants.MODEL_NAME;
    ARFModel arfModel = createARFModel(name);
    // create the root feature
    Feature root = ARFModeling.createFeature(ARFModelConstants.ROOT_UNAME, ARFModelConstants.ROOT_NAME, ARFModelConstants.ROOT_TYPE);
    arfModel.setRoot(root);
    if (model != null) {
      // convert the context variables
      convertVariables(model, arfModel, root);
      // convert priority properties
      convertProperties(model, arfModel, root);
      // convert the dimensions
      convertDimensions(model, arfModel, root);
      // convert priority rules
      convertRules(model, arfModel, root);
    }
    return arfModel;
  }

  /**
   * Convert priority rules. This maps to mandatory features and fixed-value
   * attributes.
   * 
   * @param model
   *          The DiVA variability model.
   * @param arfModel
   *          The internal ARF model.
   * @param root
   *          The ARF model root feature.
   */
  protected void convertRules(VariabilityModel model, ARFModel arfModel, Feature root) {
    // add a group feature
    Feature group = ARFModeling.createFeature(ARFModelConstants.RULES_GROUP_UNAME, ARFModelConstants.RULES_GROUP_NAME, ARFModelConstants.RULES_GROUP_TYPE);
    ARFModeling.addChild(root, group, ARFModelConstants.MANDATORY);

    EList<Rule> list = model.getRule();
    for (Rule r : list) {
      // consider priority rules only
      if (r instanceof PriorityRule) {
        PriorityRule pr = (PriorityRule) r;

        // add rule group as mandatory feature
        Feature ruleGroup = ARFModeling.createFeature(ARFModelConstants.RULE_PREFIX + ARFModelConstants.GROUP_PREFIX + ARFModeling.getId(r),
            ARFModelConstants.GROUP_NAME_PREFIX + ARFModeling.getName(r), ARFModelConstants.RULE_ELEMENT_TYPE);
        ARFModeling.addChild(group, ruleGroup, ARFModelConstants.MANDATORY);

        // add rule as alternative feature
        Feature rule = ARFModeling.createFeature(ARFModelConstants.RULE_PREFIX + ARFModeling.getId(r), ARFModeling.getName(r),
            ARFModelConstants.RULE_ELEMENT_TYPE);
        ARFModeling.addChild(ruleGroup, rule, ARFModelConstants.ALTERNATIVE);
        // convert the context expression
        convertContextExpr(pr, arfModel, rule, true);
        // convert the property priorities defined in this rule
        convertPriorities(pr, arfModel, rule);

        // add no rule as alternative feature
        Feature ruleNot = ARFModeling.createFeature(ARFModelConstants.RULE_PREFIX + ARFModelConstants.NOT_PREFIX + ARFModeling.getId(r),
            ARFModelConstants.NOT_NAME_PREFIX + ARFModeling.getName(r), ARFModelConstants.RULE_ELEMENT_TYPE);
        ARFModeling.addChild(ruleGroup, ruleNot, ARFModelConstants.ALTERNATIVE);
        // convert the context expression
        convertContextExpr(pr, arfModel, ruleNot, false);
      }
    }
  }

  /**
   * Convert the property priorities of a priority rule. This maps to feature
   * attributes.
   * 
   * @param pr
   *          The priority rule.
   * @param arfModel
   *          The internal ARF model.
   * @param rule
   *          The ARF model element.
   */
  private void convertPriorities(PriorityRule pr, ARFModel arfModel, Feature rule) {
    EList<PropertyPriority> list = pr.getPriority();
    for (PropertyPriority p : list) {
      // add as attribute with the name of the property and
      // the priority as value
      if (p.getProperty() != null) {
        ARFModeling.addAttribute(rule, ARFModeling.getId(p.getProperty()), "" + p.getPriority());
        managePriority(pr, p, arfModel, rule);
      }
    }
  }

  /**
   * Manages priority of a rule in ARF model.
   * 
   * @param pr
   *          The priority rule.
   * @param p
   *          The priority property.
   * @param rule
   *          The ARF model element.
   */
  protected void managePriority(PriorityRule pr, PropertyPriority p, ARFModel arfModel, Feature rule) {
    arfModel.addRule(rule.getName(), pr);
    arfModel.addPriority(rule.getName(), ARFModelConstants.PROPERTY_PREFIX + ARFModeling.getId(p.getProperty()), p.getPriority());
  }

  /**
   * Convert a priority rule context. Maps to a constraint on the feature.
   * 
   * @param pr
   *          The priority rule.
   * @param arfModel
   *          The internal ARF model.
   * @param feature
   *          The feature.
   * @param original
   *          False if rule shall be negated.
   */
  private void convertContextExpr(PriorityRule pr, ARFModel arfModel, Feature feature, boolean original) {
    if (pr.getContext() != null && pr.getContext().getTerm() != null) {
      // convert the term to a pvSCL expression
      String expr = ARFModeling.convertTerm(pr.getContext().getTerm());
      String content = null;
      if (original == true) {
        content = expr + " implies " + feature.getName();
      }
      else {
        content = "not(" + expr + ") implies " + feature.getName();
      }
      // add constraint with name "Context"
      ARFModeling.addConstraint(arfModel, ARFModelConstants.CONTEXT_CONSTRAINT_NAME, content);
    }
  }

  /**
   * Convert the priority properties. This maps to feature attributes.
   * 
   * @param model
   *          The DiVA variability model.
   * @param arfModel
   *          The internal ARF model.
   * @param root
   *          The ARF model root feature.
   */
  protected void convertProperties(VariabilityModel model, ARFModel arfModel, Feature root) {
    // add a group feature
    Feature group = ARFModeling.createFeature(ARFModelConstants.PROPERTIES_GROUP_UNAME, ARFModelConstants.PROPERTIES_GROUP_NAME,
        ARFModelConstants.PROPERTIES_GROUP_TYPE);
    ARFModeling.addChild(root, group, ARFModelConstants.MANDATORY);

    EList<diva.Property> list = model.getProperty();
    for (diva.Property p : list) {
      // add property as mandatory feature
      Feature feature = ARFModeling.createFeature(ARFModelConstants.PROPERTY_PREFIX + ARFModeling.getId(p), ARFModeling.getName(p),
          ARFModelConstants.PROPERTY_ELEMENT_TYPE);
      manageProperty(p, arfModel, feature);
      ARFModeling.addChild(group, feature, ARFModelConstants.MANDATORY);

      // add direction as attribute with name "Direction"
      ARFModeling.addAttribute(feature, ARFModelConstants.DIRECTION_PROPERTY_NAME, "" + p.getDirection());
    }
  }

  /**
   * Manage property in ARF model.
   * 
   * @param p
   *          The property.
   * @param property
   *          The ARF model feature.
   */
  protected void manageProperty(diva.Property p, ARFModel arfModel, Feature property) {
    arfModel.addProperty(property.getName(), p);
    arfModel.addDirection(property.getName(), p.getDirection());
  }

  /**
   * Convert the context variables. This maps to elements below the ARF model
   * root element.
   * 
   * @param model
   *          The DiVA variability model.
   * @param arfModel
   *          The internal ARF model.
   * @param root
   *          The ARF model root feature.
   */
  protected void convertVariables(VariabilityModel model, ARFModel arfModel, Feature root) {
    // add a group feature
    Feature group = ARFModeling.createFeature(ARFModelConstants.VARIABLES_GROUP_UNAME, ARFModelConstants.VARIABLES_GROUP_NAME,
        ARFModelConstants.VARIABLES_GROUP_TYPE);
    ARFModeling.addChild(root, group, ARFModelConstants.MANDATORY);

    EList<Variable> list = model.getContext();
    for (Variable v : list) {

      // get the feature ID, type, and variation type
      String elementType = null;
      String elementVariationType = null;

      if (v instanceof BooleanVariable) {
        elementType = ARFModelConstants.BOOLEAN_ELEMENT_TYPE;
        elementVariationType = ARFModelConstants.OPTIONAL;
      }
      else if (v instanceof EnumVariable) {
        elementType = ARFModelConstants.ENUMERATION_ELEMENT_TYPE;
        elementVariationType = ARFModelConstants.MANDATORY;
      }

      if (elementType != null && elementVariationType != null) {
        // add as optional feature
        Feature feature = ARFModeling.createFeature(ARFModelConstants.VARIABLE_PREFIX + ARFModeling.getId(v), ARFModeling.getName(v), elementType);
        ARFModeling.addChild(group, feature, elementVariationType);

        // put the literals as alternative group below this feature
        // if this is an enumeration variable
        if (v instanceof EnumVariable) {
          convertLiterals(arfModel, feature, (EnumVariable) v);
        }
      }
    }
  }

  /**
   * Convert enumeration literals. Maps to alternative elements below the
   * element representing the enumeration.
   * 
   * @param arfModel
   *          The internal ARF model.
   * @param parent
   *          The enumeration element.
   * @param v
   *          The enumeration variable.
   */
  private void convertLiterals(ARFModel arfModel, Feature parent, EnumVariable v) {
    EList<EnumLiteral> list = v.getLiteral();
    for (EnumLiteral l : list) {
      // add as alternative feature
      Feature element = ARFModeling
          .createFeature(parent.getName() + "_" + ARFModeling.getId(l), ARFModeling.getName(l), ARFModelConstants.LITERAL_ELEMENT_TYPE);
      ARFModeling.addChild(parent, element, ARFModelConstants.ALTERNATIVE);
    }
  }

  /**
   * Convert dimensions. This maps to the top level features.
   * 
   * @param model
   *          The DiVA variability model.
   * @param arfModel
   *          The internal ARF model.
   * @param root
   *          The ARF model root feature.
   */
  protected void convertDimensions(VariabilityModel model, ARFModel arfModel, Feature root) {
    // add a group feature
    Feature group = ARFModeling.createFeature(ARFModelConstants.DIMENSIONS_GROUP_UNAME, ARFModelConstants.DIMENSIONS_GROUP_NAME,
        ARFModelConstants.DIMENSIONS_GROUP_TYPE);
    ARFModeling.addChild(root, group, ARFModelConstants.MANDATORY);

    EList<Dimension> list = model.getDimension();
    for (Dimension d : list) {
      Feature feature = ARFModeling.createFeature(ARFModelConstants.DIMENSION_PREFIX + ARFModeling.getId(d), ARFModeling.getName(d),
          ARFModelConstants.DIMENSION_ELEMENT_TYPE);

      if (d.getLower() >= 1) {
        // add as mandatory feature if at least one
        // sub-feature has to be selected
        ARFModeling.addChild(group, feature, ARFModelConstants.MANDATORY);
      }
      else {
        // add as optional feature, none of the sub-features
        // need to be selected
        ARFModeling.addChild(group, feature, ARFModelConstants.OPTIONAL);
      }

      // convert property list
      convertPropertyList(d, feature);
      // convert the sub-features
      convertVariants(d, arfModel, feature);
    }
  }

  /**
   * Convert the attribute property on the dimension element. Maps to a list
   * attribute on the corresponding ARF model feature.
   * 
   * @param d
   *          The dimension element.
   * @param feature
   *          The ARF model feature.
   */
  private void convertPropertyList(Dimension d, Feature feature) {
    StringBuffer content = new StringBuffer();
    EList<diva.Property> list = d.getProperty();
    for (diva.Property p : list) {
      content.append(ARFModeling.getId(p));
      content.append(";");
    }
    ARFModeling.addAttribute(feature, ARFModelConstants.PROPERTIES_PROPERTY_NAME, content.toString());
  }

  /**
   * Convert variants. This maps to the second level features.
   * 
   * @param d
   *          The current dimension.
   * @param arfModel
   *          The internal ARF model.
   * @param parent
   *          The element representing the current dimension.
   */
  private void convertVariants(Dimension d, ARFModel arfModel, Feature parent) {
    EList<Variant> list = d.getVariant();
    for (Variant v : list) {
      Feature feature = ARFModeling.createFeature(ARFModelConstants.VARIANT_PREFIX + ARFModeling.getId(v), ARFModeling.getName(v),
          ARFModelConstants.VARIANT_ELEMENT_TYPE);
      if (d.getUpper() == 1) {
        // add as alternative feature if at least one
        // feature of this group has to be selected
        ARFModeling.addChild(parent, feature, ARFModelConstants.ALTERNATIVE);
      }
      else {
        // add as or-feature and set the given range
        ARFModeling.addChild(parent, feature, d.getLower() == 0 ? 1 : d.getLower(), d.getUpper());
      }
      manageVariant(v, arfModel, feature);

      // convert constraints
      convertAvailable(v, arfModel, feature);
      convertDependency(v, arfModel, feature);
      convertRequired(v, arfModel, feature);

      // convert properties
      convertPropertyValues(v, arfModel, feature);
    }
  }

  /**
   * Manages variant in ARF model.
   * 
   * @param v
   *          The variant.
   * @param arfModel
   *          The internal ARF model.
   * @param variant
   *          The ARF model element.
   */
  protected void manageVariant(Variant v, ARFModel arfModel, Feature variant) {
    arfModel.addVariant(variant.getName(), v);
  }

  /**
   * Convert property values of a variant element. This maps to attributes on
   * the corresponding ARF model feature.
   * 
   * @param v
   *          The variant element.
   * @param arfModel
   *          The internal ARF model.
   * @param feature
   *          The ARF model feature.
   */
  private void convertPropertyValues(Variant v, ARFModel arfModel, Feature feature) {
    EList<PropertyValue> list = v.getPropertyValue();
    for (PropertyValue pv : list) {
      if (pv.getProperty() != null) {
        // add as attribute with the name of the property
        ARFModeling.addAttribute(feature, ARFModeling.getId(pv.getProperty()), "" + pv.getValue());
        manageVariantProperty(v, pv, arfModel, feature);
      }
    }
  }

  /**
   * Manages variant property in ARF model.
   * 
   * @param v
   *          The variant.
   * @param pv
   *          The property value.
   * @param arfModel
   *          The internal ARF model.
   * @param variant
   *          The ARF model feature.
   */
  protected void manageVariantProperty(Variant v, PropertyValue pv, ARFModel arfModel, Feature variant) {
    arfModel.addVariant(variant.getName(), v);
    arfModel.addValue(variant.getName(), ARFModelConstants.PROPERTY_PREFIX + ARFModeling.getId(pv.getProperty()), pv.getValue());
  }

  /**
   * Convert a 'required' constraint. Maps to a constraint on the feature.
   * 
   * @param v
   *          The variant element.
   * @param arfModel
   *          The internal ARF model.
   * @param feature
   *          The ARF model feature.
   */
  private void convertRequired(Variant v, ARFModel arfModel, Feature feature) {
    if (v.getRequired() != null && v.getRequired().getTerm() != null) {
      // convert the term to a pvSCL expression
      String expr = ARFModeling.convertTerm(v.getRequired().getTerm());
      String content = expr + " implies " + feature.getName();
      // add constraint with name "Required"
      ARFModeling.addConstraint(arfModel, ARFModelConstants.REQUIRED_CONSTRAINT_NAME, content);
    }
  }

  /**
   * Convert a 'dependency' constraint. Maps to a constraint on the feature.
   * 
   * @param v
   *          The variant element.
   * @param arfModel
   *          The internal ARF model.
   * @param element
   *          The ARF model element.
   */
  private void convertDependency(Variant v, ARFModel arfModel, Feature feature) {
    if (v.getDependency() != null && v.getDependency().getTerm() != null) {
      // convert the term to a pvSCL expression
      String expr = ARFModeling.convertTerm(v.getDependency().getTerm());
      String content = feature.getName() + " implies " + expr;
      // add constraint with name "Dependency"
      ARFModeling.addConstraint(arfModel, ARFModelConstants.DEPENDENCY_CONSTRAINT_NAME, content);
    }
  }

  /**
   * Convert an 'available' constraint. Maps to a constraint on the feature.
   * 
   * @param v
   *          The variant element.
   * @param arfModel
   *          The internal ARF model.
   * @param feature
   *          The ARF model feature.
   */
  private void convertAvailable(Variant v, ARFModel arfModel, Feature feature) {
    if (v.getAvailable() != null && v.getAvailable().getTerm() != null) {
      // convert the term to a pvSCL expression
      String expr = ARFModeling.convertTerm(v.getAvailable().getTerm());
      String content = feature.getName() + " implies " + expr;
      // add constraint with name "Available"
      ARFModeling.addConstraint(arfModel, ARFModelConstants.AVAILABLE_CONSTRAINT_NAME, content);
    }
  }

}
