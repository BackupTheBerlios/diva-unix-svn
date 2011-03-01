package arf.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.featuremodel.FeatureModel;

import diva.PriorityRule;
import diva.Property;
import diva.Variant;

public class ARFModel extends ARFSystem implements FeatureModel {
  /**
   * Maps for property values, priorities, and directions.
   */
  private Map<String, Integer>      m_Priorities = new HashMap<String, Integer>();
  private Map<String, Integer>      m_Values     = new HashMap<String, Integer>();
  private Map<String, Integer>      m_Directions = new HashMap<String, Integer>();
  /**
   * Maps for the properties and variants.
   */
  private Map<String, Property>     m_Properties = new HashMap<String, Property>();
  private Map<String, Variant>      m_Variants   = new HashMap<String, Variant>();
  private Map<String, PriorityRule> m_Rules      = new HashMap<String, PriorityRule>();

  public ARFModel() {
    super();
  }

  public ARFModel(ARFModel model) {
    super(model);
    m_Priorities.putAll(model.m_Priorities);
    m_Values.putAll(model.m_Values);
    m_Directions.putAll(model.m_Directions);
    m_Properties.putAll(model.m_Properties);
    m_Variants.putAll(model.m_Variants);
    m_Rules.putAll(model.m_Rules);
  }

  private String getKey(String k1, String k2) {
    return k1 + "@" + k2;
  }

  public void addPriority(String ruleID, String propertyID, int priority) {
    m_Priorities.put(getKey(ruleID, propertyID), priority);
  }

  public void addValue(String variantID, String propertyID, int value) {
    m_Values.put(getKey(variantID, propertyID), value);
  }

  public void addDirection(String propertyID, int direction) {
    m_Directions.put(propertyID, direction);
  }

  public void addProperty(String propertyID, Property property) {
    m_Properties.put(propertyID, property);
  }

  public void addVariant(String variantID, Variant variant) {
    m_Variants.put(variantID, variant);
  }

  public void addRule(String ruleID, PriorityRule rule) {
    m_Rules.put(ruleID, rule);
  }

  public int getPriority(String ruleID, String propertyID) {
    Integer priority = m_Priorities.get(getKey(ruleID, propertyID));
    return priority != null ? priority : 0;
  }

  public int getValue(String variantID, String propertyID) {
    Integer value = m_Values.get(getKey(variantID, propertyID));
    return value != null ? value : 0;
  }

  public int getDirection(String propertyID) {
    Integer direction = m_Directions.get(propertyID);
    return direction != null ? direction : 0;
  }

  public Map<String, Property> getProperties() {
    return m_Properties;
  }

  public Map<String, Variant> getVariants() {
    return m_Variants;
  }

  public Map<String, PriorityRule> getRules() {
    return m_Rules;
  }

}
