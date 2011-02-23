package arf.model.util;

public class ARFModelConstants {

  /**
   * Constant for the name of temporary reasoning scenario. Will be created
   * while reasoning and has to be removed after reasoning.
   */
  static public String       TEMPORARY_REASONING_SCENARIO = "ARFScenario4Reasoning";
  /**
   * Constant for the name of temporary reasoning oracle. Will be created while
   * reasoning and has to be removed after reasoning.
   */
  static public String       TEMPORARY_REASONING_ORACLE   = "ARFOracle4Reasoning";
  /**
   * Constant for the start of reasoning oracle. Will be created while reasoning
   * and has to be removed after reasoning.
   */
  static public String       REASONING_ORACLE_START       = "__ARF__";

  /**
   * Types and names.
   */
  static public final String MODEL_NAME                   = "VariabilityModel";
  static public final String CONTEXT_NAME                 = "ContextModel";
  static public final String CONFIGURATION_NAME           = "ConfigurationModel";

  static public final String DIMENSION_ELEMENT_TYPE       = "diva:dimension";
  static public final String VARIANT_ELEMENT_TYPE         = "diva:variant";
  static public final String RULE_ELEMENT_TYPE            = "diva:rule";
  static public final String ORACLE_ELEMENT_TYPE          = "diva:oracle";
  static public final String PROPERTY_ELEMENT_TYPE        = "diva:property";
  static public final String BOOLEAN_ELEMENT_TYPE         = "diva:boolean";
  static public final String ENUMERATION_ELEMENT_TYPE     = "diva:enumeration";
  static public final String LITERAL_ELEMENT_TYPE         = "diva:literal";

  static public final String DEPENDENCY_CONSTRAINT_NAME   = "Dependency";
  static public final String AVAILABLE_CONSTRAINT_NAME    = "Available";
  static public final String REQUIRED_CONSTRAINT_NAME     = "Required";
  static public final String CONTEXT_CONSTRAINT_NAME      = "Context";
  static public final String ORACLE_CONSTRAINT_NAME       = "Oracle";

  static public final String ROOT_TYPE                    = "diva:model";
  static public final String ROOT_NAME                    = "Variability Model";
  static public final String ROOT_UNAME                   = "VariabilityModel";

  static public final String DIMENSIONS_GROUP_TYPE        = "diva:dimensions";
  static public final String DIMENSIONS_GROUP_NAME        = "Dimensions";
  static public final String DIMENSIONS_GROUP_UNAME       = "Dimensions_Group";

  static public final String VARIABLES_GROUP_TYPE         = "diva:variables";
  static public final String VARIABLES_GROUP_NAME         = "Variables";
  static public final String VARIABLES_GROUP_UNAME        = "Variables_Group";

  static public final String PROPERTIES_GROUP_TYPE        = "diva:properties";
  static public final String PROPERTIES_GROUP_NAME        = "Properties";
  static public final String PROPERTIES_GROUP_UNAME       = "Properties_Group";

  static public final String RULES_GROUP_TYPE             = "diva:rules";
  static public final String RULES_GROUP_NAME             = "Rules";
  static public final String RULES_GROUP_UNAME            = "Rules_Group";

  static public final String ORACLES_GROUP_TYPE           = "diva:oracles";
  static public final String ORACLES_GROUP_NAME           = "Oracles";
  static public final String ORACLES_GROUP_UNAME          = "Oracles_Group";

  static public final String PROPERTIES_PROPERTY_NAME     = "Properties";
  static public final String PRIORITY_PROPERTY_NAME       = "Priority";
  static public final String DIRECTION_PROPERTY_NAME      = "Direction";
  static public final String VALUE_PROPERTY_NAME          = "Value";

  /**
   * Element prefixes.
   */
  static public final String VARIABLE_PREFIX              = "Variable_";
  static public final String PROPERTY_PREFIX              = "Property_";
  static public final String DIMENSION_PREFIX             = "Dimension_";
  static public final String VARIANT_PREFIX               = "Variant_";
  static public final String RULE_PREFIX                  = "Rule_";
  static public final String ORACLE_PREFIX                = "Oracle_";
  static public final String GROUP_PREFIX                 = "Group_";
  static public final String GROUP_NAME_PREFIX            = "Group of ";
  static public final String NOT_PREFIX                   = "No_";
  static public final String NOT_NAME_PREFIX              = "Not ";

  /**
   * ARF models constants
   */
  static public final String MODEL_NAME_ATTRIBUTE         = "diva:name";
  static public final String MANDATORY                    = "diva:mandatory";
  static public final String OPTIONAL                     = "diva:optional";
  static public final String ALTERNATIVE                  = "diva:alternative";
  static public final String OR                           = "diva:or";

}
