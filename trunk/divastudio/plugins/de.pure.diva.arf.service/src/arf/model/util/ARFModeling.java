package arf.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.featuremodel.Attribute;
import org.eclipse.featuremodel.AttributeTypeString;
import org.eclipse.featuremodel.AttributeValueString;
import org.eclipse.featuremodel.Constraint;
import org.eclipse.featuremodel.Description;
import org.eclipse.featuremodel.Feature;
import org.eclipse.featuremodel.FeatureModel;
import org.eclipse.featuremodel.FeatureModelFactory;
import org.eclipse.featuremodel.Group;
import org.eclipse.featuremodel.VariabilityType;
import org.eclipse.variantmodel.SelectionState;
import org.eclipse.variantmodel.VariantModel;
import org.eclipse.variantmodel.VariantModelFactory;
import org.eclipse.variantmodel.VariantSelection;

import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.ARFVariant;
import arf.model.IDGenerator;
import diva.AndTerm;
import diva.BooleanTerm;
import diva.DivaFactory;
import diva.EnumTerm;
import diva.NamedElement;
import diva.NaryTerm;
import diva.NotTerm;
import diva.OrTerm;
import diva.Scenario;
import diva.Term;
import diva.VariabilityModel;
import diva.VariantTerm;

public class ARFModeling {

  public static Feature getParent(Feature feature) {
    Feature parent = null;
    Group group = getParentGroup(feature);
    if (group != null) {
      EObject container = group.eContainer();
      if (container instanceof Feature) {
        parent = (Feature) container;
      }
    }
    return parent;
  }

  public static Group getParentGroup(Feature feature) {
    Group group = null;
    EObject container = feature.eContainer();
    if (container instanceof Group) {
      group = (Group) container;
    }
    return group;
  }

  public static FeatureModel getModel(Feature feature) {
    FeatureModel model = null;
    EObject container = feature.eContainer();
    if (container instanceof FeatureModel) {
      model = (FeatureModel) container;
    }
    else {
      Feature parent = getParent(feature);
      if (parent != null) {
        model = getModel(parent);
      }
    }
    return model;
  }

  public VariabilityType getVariabilityType(Feature feature) {
    VariabilityType type = VariabilityType.MANDATORY;
    Group group = getParentGroup(feature);
    if (group != null) {
      int lower = group.getLower();
      int upper = group.getUpper();
      if (lower == -1 && upper == -1) {
        type = VariabilityType.MANDATORY;
      }
      else if (lower == 0 && upper == -1) {
        type = VariabilityType.OPTIONAL;
      }
      else if (lower == 1 && upper == 1) {
        type = VariabilityType.ALTERNATIVE;
      }
      else {
        type = VariabilityType.OR;
      }
    }
    return type;
  }

  public static Scenario getReasoningScenario(VariabilityModel model) {
    Scenario result = null;
    if (model.getSimulation().getScenario().isEmpty() == false) {
      for (Scenario scenario : model.getSimulation().getScenario()) {
        if (ARFModelConstants.TEMPORARY_REASONING_SCENARIO.equals(scenario.getName()) == true) {
          result = scenario;
          break;
        }
      }
    }
    return result;
  }

  public static Scenario createReasoningScenario(VariabilityModel model) {
    Scenario scenario = getReasoningScenario(model);
    if (scenario == null) {
      scenario = DivaFactory.eINSTANCE.createScenario();
      scenario.setName(ARFModelConstants.TEMPORARY_REASONING_SCENARIO);
      model.getSimulation().getScenario().add(0, scenario);
    }
    else {
      scenario.getContext().clear();
    }
    return scenario;
  }

  public static boolean hasReasoningScenario(VariabilityModel model) {
    return getReasoningScenario(model) != null;
  }

  public static void removeReasoningScenario(VariabilityModel model) {
    Scenario scenario = getReasoningScenario(model);
    if (scenario != null) {
      model.getSimulation().getScenario().remove(scenario);
    }
  }

  public static String getId(NamedElement e) {
    String id = "";
    if (e != null) {
      id = e.getId();
      if (id == null || "".equals(id) == true) {
        id = IDGenerator.generate();
        id = id.replaceAll("-", "__");
        e.setId(id);
        // set resource modified
        e.eResource().setModified(true);
      }
    }
    return id;
  }

  public static String getName(NamedElement e) {
    String name = "";
    if (e != null) {
      name = e.getName();
      if (name == null || "".equals(name) == true) {
        name = e.getId();
        if (name == null || "".equals(name) == true) {
          name = IDGenerator.generate();
          name = name.replaceAll("-", "__");
          e.setName(name);
          // set resource modified
          e.eResource().setModified(true);
        }
        else {
          e.setName(name);
          // set resource modified
          e.eResource().setModified(true);
        }
      }
    }
    return name;
  }

  /**
   * Create a feature.
   * 
   * @param uname
   *          Unique name of the element.
   * @param name
   *          Visible name of the element.
   * @param type
   *          Element type.
   * @return The feature.
   */
  public static Feature createFeature(String uname, String name, String type) {
    Feature feature = FeatureModelFactory.eINSTANCE.createFeature();
    feature.setId(IDGenerator.generate());
    feature.setName(uname);
    feature.setType(type);
    Description description = FeatureModelFactory.eINSTANCE.createDescription();
    description.setId(IDGenerator.generate());
    description.setText(name);
    feature.setDescription(description);
    return feature;
  }

  /**
   * Remove a feature as child of another feature.
   * 
   * @param parent
   *          The parent feature.
   * @param child
   *          The child feature.
   */
  public static void remChild(Feature parent, Feature child) {
    for (Group children : parent.getChildren()) {
      children.getFeatures().remove(child);
    }
  }

  /**
   * Add an element as child of another element.
   * 
   * @param parent
   *          The parent element.
   * @param child
   *          The child element.
   * @param type
   *          The variability type.
   */
  public static void addChild(Feature parent, Feature child, String type) {
    int lower = Range.UNBOUNDED;
    int upper = Range.UNBOUNDED;
    if (ARFModelConstants.OPTIONAL.equals(type) == true) {
      lower = 0;
    }
    else if (ARFModelConstants.ALTERNATIVE.equals(type) == true) {
      lower = 1;
      upper = 1;
    }
    addChild(parent, child, lower, upper);
  }

  /**
   * Add an element as child of another element.
   * 
   * @param parent
   *          The parent element.
   * @param child
   *          The child element.
   * @param lower
   *          The lower bound.
   * @param upper
   *          The upper bound.
   */
  public static void addChild(Feature parent, Feature child, int lower, int upper) {
    Group group = null;
    for (Group children : parent.getChildren()) {
      if (children.getLower() == lower && children.getUpper() == upper) {
        group = children;
        break;
      }
    }
    if (group == null) {
      group = FeatureModelFactory.eINSTANCE.createGroup();
      group.setId(IDGenerator.generate());
      group.setLower(lower);
      group.setUpper(upper);
      parent.getChildren().add(group);
    }
    if (group.getFeatures().contains(child) == false) {
      group.getFeatures().add(child);
    }
  }

  /**
   * Add a constraint to an ARFModel.
   * 
   * @param model
   *          The ARFModel.
   * @param name
   *          The name of the constraint.
   * @param content
   *          The constraint expression.
   */
  public static void addConstraint(ARFModel model, String name, String content) {
    Constraint constraint = FeatureModelFactory.eINSTANCE.createConstraint();
    constraint.setId(IDGenerator.generate());
    constraint.setLanguage("diva:scl");
    Description description = FeatureModelFactory.eINSTANCE.createDescription();
    description.setId(IDGenerator.generate());
    description.setText(name);
    constraint.setCode(content);
    model.getConstraints().add(constraint);
  }

  /**
   * Add a string attribute with the given name to a feature.
   * 
   * @param feature
   *          The feature.
   * @param name
   *          The attribute name.
   * @param value
   *          The attribute value.
   */
  public static void addAttribute(Feature feature, String name, String value) {
    Attribute attribute = FeatureModelFactory.eINSTANCE.createAttribute();
    attribute.setId(IDGenerator.generate());
    attribute.setName(name);
    attribute.setSetable(false);
    AttributeTypeString t = FeatureModelFactory.eINSTANCE.createAttributeTypeString();
    attribute.setType(t);
    AttributeValueString v = FeatureModelFactory.eINSTANCE.createAttributeValueString();
    v.setValue(value);
    attribute.setDefaultValue(v);
  }

  /**
   * Convert a DiVA constraint term to pvSCL.
   * 
   * @param term
   *          The term to convert.
   * @return The pvSCL expression.
   */
  public static String convertTerm(Term term) {
    String expr = "";
    if (term instanceof AndTerm) {
      AndTerm at = (AndTerm) term;
      expr = convertNaryTerm(at, "and");
    }
    else if (term instanceof OrTerm) {
      OrTerm at = (OrTerm) term;
      expr = convertNaryTerm(at, "or");
    }
    else if (term instanceof BooleanTerm && ((BooleanTerm) term).getVariable() != null) {
      BooleanTerm bt = (BooleanTerm) term;
      expr = ARFModelConstants.VARIABLE_PREFIX + bt.getVariable().getId();
    }
    else if (term instanceof EnumTerm && ((EnumTerm) term).getVariable() != null && ((EnumTerm) term).getValue() != null) {
      EnumTerm et = (EnumTerm) term;
      expr = ARFModelConstants.VARIABLE_PREFIX + et.getVariable().getId() + "_" + et.getValue().getId();
    }
    else if (term instanceof NotTerm) {
      NotTerm nt = (NotTerm) term;
      expr = "not(" + convertTerm(nt.getTerm()) + ")";
    }
    else if (term instanceof VariantTerm && ((VariantTerm) term).getVariant() != null) {
      VariantTerm vt = (VariantTerm) term;
      expr = ARFModelConstants.VARIANT_PREFIX + vt.getVariant().getId();
    }
    return expr;
  }

  /**
   * Convert an AND and OR term.
   * 
   * @param term
   *          The term to convert.
   * @param op
   *          The actual operator name.
   * @return The expression.
   */
  private static String convertNaryTerm(NaryTerm term, String op) {
    String expr = "(";
    boolean first = true;
    for (Term t : term.getTerm()) {
      if (!first) {
        expr += " " + op + " ";
      }
      first = false;
      expr += convertTerm(t);
    }
    expr += ")";
    return expr;
  }

  /**
   * Check if the feature with the given name is selected.
   * 
   * @param arfConfiguration
   *          The ARF configuration model.
   * @param arfModel
   *          The ARF reasoning model.
   * @param name
   *          The name of feature.
   * @return True if the feature is selected.
   */
  public static boolean isSelected(ARFConfiguration arfConfiguration, ARFModel arfModel, String name) {
    Feature feature = getFeatureWithName(arfModel, name);
    VariantSelection selection = getVariantSelection(arfConfiguration, feature);
    return selection != null && selection.getState().getLiteral().equals(SelectionState.SELECTED.getLiteral());
  }

  /**
   * Check if the feature with the given name is excluded.
   * 
   * @param arfConfiguration
   *          The ARF configuration model.
   * @param arfModel
   *          The ARF reasoning model.
   * @param name
   *          The name of feature.
   * @return True if the feature is excluded.
   */
  public static boolean isExcluded(ARFConfiguration arfConfiguration, ARFModel arfModel, String name) {
    Feature feature = getFeatureWithName(arfModel, name);
    VariantSelection selection = getVariantSelection(arfConfiguration, feature);
    return selection != null && selection.getState().getLiteral().equals(SelectionState.EXCLUDED.getLiteral());
  }

  /**
   * Check if the feature with the given name is a selection.
   * 
   * @param arfConfiguration
   *          The ARF configuration model.
   * @param arfModel
   *          The ARF reasoning model.
   * @param name
   *          The name of feature.
   * @return True if the feature is a selection.
   */
  public static boolean isSelection(ARFConfiguration arfConfiguration, ARFModel arfModel, String name) {
    Feature feature = getFeatureWithName(arfModel, name);
    VariantSelection selection = getVariantSelection(arfConfiguration, feature);
    return selection != null;
  }

  private static VariantSelection getVariantSelection(ARFConfiguration arfConfiguration, Feature feature) {
    VariantSelection selection = null;
    if (arfConfiguration != null) {
      for (VariantSelection s : arfConfiguration.getSelections()) {
        if (s.getFeature() != null && feature != null && (s.getFeature() == feature || s.getFeature().getName().equals(feature.getName()) == true)) {
          selection = s;
          break;
        }
      }
    }
    return selection;
  }

  private static Attribute getModelName(FeatureModel model) {
    Attribute attribute = null;
    for (Attribute a : model.getAttributes()) {
      if (ARFModelConstants.MODEL_NAME_ATTRIBUTE.equals(a.getName()) == true) {
        attribute = a;
        break;
      }
    }
    return attribute;
  }

  public static void setModelName(FeatureModel model, String name) {
    Attribute attribute = getModelName(model);
    AttributeValueString value = FeatureModelFactory.eINSTANCE.createAttributeValueString();
    value.setValue(name);
    if (attribute != null) {
      attribute.setDefaultValue(value);
    }
    else {
      attribute = FeatureModelFactory.eINSTANCE.createAttribute();
      attribute.setId(IDGenerator.generate());
      attribute.setName(ARFModelConstants.MODEL_NAME_ATTRIBUTE);
      attribute.setSetable(false);
      AttributeTypeString type = FeatureModelFactory.eINSTANCE.createAttributeTypeString();
      attribute.setType(type);
      attribute.setDefaultValue(value);
      model.getAttributes().add(attribute);
    }
  }

  private static Attribute getModelName(VariantModel model) {
    Attribute attribute = null;
    for (Attribute a : model.getAttributes()) {
      if (ARFModelConstants.MODEL_NAME_ATTRIBUTE.equals(a.getName()) == true) {
        attribute = a;
        break;
      }
    }
    return attribute;
  }

  public static void setModelName(VariantModel model, String name) {
    Attribute attribute = getModelName(model);
    AttributeValueString value = FeatureModelFactory.eINSTANCE.createAttributeValueString();
    value.setValue(name);
    if (attribute != null) {
      attribute.setDefaultValue(value);
    }
    else {
      attribute = FeatureModelFactory.eINSTANCE.createAttribute();
      attribute.setId(IDGenerator.generate());
      attribute.setName(ARFModelConstants.MODEL_NAME_ATTRIBUTE);
      attribute.setSetable(false);
      AttributeTypeString type = FeatureModelFactory.eINSTANCE.createAttributeTypeString();
      attribute.setType(type);
      attribute.setDefaultValue(value);
      model.getAttributes().add(attribute);
    }
  }

  /**
   * Add a selection to the ARF context for the element with the given name.
   * 
   * @param arfContext
   *          The ARF context.
   * @param uname
   *          The unique name of the element to select.
   * @param arfModel
   *          The ARF model.
   * @param state
   *          The selection state.
   * @return The variant selection.
   */
  public static VariantSelection addSelection(ARFContext arfContext, String uname, ARFModel arfModel, SelectionState state) {
    return addSelection((ARFVariant) arfContext, uname, arfModel, state);
  }

  /**
   * Add a selection to the ARF configuration for the element with the given
   * name.
   * 
   * @param arfConfiguration
   *          The ARF configuration.
   * @param uname
   *          The unique name of the element to select.
   * @param arfModel
   *          The ARF model.
   * @param state
   *          The selection state.
   * @return The variant selection.
   */
  public static VariantSelection addSelection(ARFConfiguration arfConfiguration, String uname, ARFModel arfModel, SelectionState state) {
    return addSelection((ARFVariant) arfConfiguration, uname, arfModel, state);
  }

  /**
   * Add a selection to a variant model for the feature with the given name.
   * 
   * @param variant
   *          The variant model.
   * @param uname
   *          The unique name of the feature to select.
   * @param arfModel
   *          The ARF model.
   * @param state
   *          The selection state.
   * @return The variant selection.
   */
  private static VariantSelection addSelection(ARFVariant variant, String uname, ARFModel arfModel, SelectionState state) {
    VariantSelection selection = null;
    // find the feature with the given name in the model
    Feature feature = getFeatureWithName(arfModel, uname);
    if (feature != null) {
      // create a new variant selection for the feature
      selection = createVariantSelection(variant, feature, state);
    }
    return selection;
  }

  public static List<Feature> getFeatures(ARFModel model) {
    List<Feature> features = new ArrayList<Feature>();
    Feature root = model.getRoot();
    features.add(root);
    getFeatures(features, root);
    return features;
  }

  private static void getFeatures(List<Feature> features, Feature element) {
    for (Group group : element.getChildren()) {
      for (Feature child : group.getFeatures()) {
        features.add(child);
        getFeatures(features, child);
      }
    }
  }

  /**
   * Get feature of a given name;
   * 
   * @param arfModel
   *          The ARF model.
   * @param uname
   *          The unique name of the feature to select.
   * @return The feature.
   */
  public static Feature getFeatureWithName(ARFModel model, String uname) {
    Feature parent = model.getRoot();
    return getFeatureWithName(parent, uname);
  }

  private static Feature getFeatureWithName(Feature parent, String uname) {
    Feature feature = null;
    if (parent != null && uname.equals(parent.getName())) {
      feature = parent;
    }
    else {
      for (Group children : parent.getChildren()) {
        for (Feature child : children.getFeatures()) {
          feature = getFeatureWithName(child, uname);
          if (feature != null) {
            break;
          }
        }
        if (feature != null) {
          break;
        }
      }
    }
    return feature;
  }

  /**
   * Create a new variant selection for the given feature.
   * 
   * @param variant
   *          The variant model.
   * @param feature
   *          The selected feature.
   * @param state
   *          The selection state.
   * @return The variant selection.
   */
  private static VariantSelection createVariantSelection(ARFVariant variant, Feature feature, SelectionState state) {
    VariantSelection selection = VariantModelFactory.eINSTANCE.createVariantSelection();
    selection.setId(IDGenerator.generate());
    selection.setBound(true);
    selection.setState(state);
    selection.setFeature(feature);
    variant.getSelections().add(selection);
    return selection;
  }

  /**
   * Get all children by children type of a feature.
   * 
   * @param feature
   *          The parent feature.
   * @return A map with children type as key and a list of children as value.
   */
  public static Map<String, List<Feature>> getChildren(Feature parent) {
    Map<String, List<Feature>> children = new HashMap<String, List<Feature>>();
    children.put(ARFModelConstants.MANDATORY, new ArrayList<Feature>());
    children.put(ARFModelConstants.OPTIONAL, new ArrayList<Feature>());
    children.put(ARFModelConstants.ALTERNATIVE, new ArrayList<Feature>());
    children.put(ARFModelConstants.OR, new ArrayList<Feature>());
    children.put("", new ArrayList<Feature>());
    for (Group group : parent.getChildren()) {
      List<Feature> list = null;
      if (group.getLower() == Range.UNBOUNDED && group.getUpper() == Range.UNBOUNDED) {
        list = children.get(ARFModelConstants.MANDATORY);
      }
      else if (group.getLower() == 0 && group.getUpper() == Range.UNBOUNDED) {
        list = children.get(ARFModelConstants.OPTIONAL);
      }
      else if (group.getLower() == 1 && group.getUpper() == 1) {
        list = children.get(ARFModelConstants.ALTERNATIVE);
      }
      else if (group.getLower() > 0 && group.getUpper() == Range.UNBOUNDED) {
        list = children.get(ARFModelConstants.OR);
      }
      else {
        list = children.get("");
      }
      for (Feature feature : group.getFeatures()) {
        list.add(feature);
      }
    }
    return children;
  }

  public static class Range {
    public static final int UNBOUNDED = -1;
    public int              lower     = UNBOUNDED;
    public int              upper     = UNBOUNDED;
  }

  public static Range getRange(String s) {
    Range range = new Range();
    if (s != null) {
      s = s.replaceAll("\\[", "");
      s = s.replaceAll("\\]", "");
      String[] split = s.split(",");
      if (split.length == 1) {
        if ("n".equals(split[0]) == false && "".equals(split[0]) == false) {
          range.lower = Integer.parseInt(split[0]);
          range.upper = Integer.parseInt(split[0]);
        }
      }
      else if (split.length > 1) {
        if ("n".equals(split[0]) == false && "".equals(split[0]) == false) {
          range.lower = Integer.parseInt(split[0]);
        }
        if ("n".equals(split[1]) == false && "".equals(split[1]) == false) {
          range.upper = Integer.parseInt(split[1]);
        }
      }
    }
    return range;
  }

  public static Range getRange(Feature feature) {
    String range = null;
    for (Group group : feature.getChildren()) {
      if (group.getLower() > 0 && group.getUpper() != 1) {
        int lower = group.getLower();
        int upper = group.getUpper();
        range = "[" + lower + ",";
        if (upper == Range.UNBOUNDED || upper == 0) {
          range += "n]";
        }
        else {
          range += upper + "]";
        }
      }
    }
    return getRange(range);
  }

  public static String getElementName(VariantSelection selection) {
    String name = selection.getId();
    if (selection.getFeature() != null) {
      name = selection.getFeature().getName();
    }
    return name;
  }

  public static String getQualifiedName(Feature feature) {
    String name = feature.getName();
    FeatureModel model = getModel(feature);
    if (model != null) {
      name = ((AttributeValueString) (getModelName(model).getDefaultValue())).getValue() + "." + name;
    }
    return name;
  }

}
