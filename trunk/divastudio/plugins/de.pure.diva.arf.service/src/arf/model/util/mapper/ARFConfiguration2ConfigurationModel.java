package arf.model.util.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import arf.model.ARFConfiguration;
import arf.model.ARFModel;
import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;
import diva.ConfigVariant;
import diva.Configuration;
import diva.Context;
import diva.DivaFactory;
import diva.Priority;
import diva.Property;
import diva.Scenario;
import diva.Score;
import diva.VariabilityModel;
import diva.Variant;
import diva.Verdict;

public class ARFConfiguration2ConfigurationModel {
  private static int           count            = 0;
  /**
   * Priority weight K.
   */
  private static int           K                = 2;
  /**
   * Collected score calculation data.
   */
  private Map<String, Integer> propertyPriority = null;

  public ARFConfiguration2ConfigurationModel(Map<String, Integer> priorities) {
    propertyPriority = priorities;
  }

  private Resource getResource(Configuration configuration, URI uri) {
    // Create a resource set
    int totalScore = configuration.getTotalScore();
    diva_configuration.SuitableConfigurations configurationModel = diva_configuration.Diva_configurationFactory.eINSTANCE.createSuitableConfigurations();
    diva_configuration.Configuration suitableConfiguration = diva_configuration.Diva_configurationFactory.eINSTANCE.createConfiguration();
    suitableConfiguration.setScore(totalScore);
    configurationModel.getConfigurations().add(suitableConfiguration);
    EList<ConfigVariant> variants = configuration.getVariant();
    for (ConfigVariant variant : variants) {
      String name = variant.getVariant().getName();
      diva_configuration.Aspect aspect = diva_configuration.Diva_configurationFactory.eINSTANCE.createAspect();
      aspect.setName(name);
      suitableConfiguration.getAspect().add(aspect);
    }
    // Create a resource for this file.
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.createResource(URI.createURI((uri != null ? uri.toString() : "") + "_result_" + count + ".xmi"));
    if (resource != null) {
      // Add the initial model object to the contents.
      resource.getContents().add(configurationModel);
    }
    return resource;
  }

  /**
   * Convert the given internal ARF configuration to a configuration.
   * 
   * @param arfConfiguration
   *          The internal ARF configuration.
   * @param arfModel
   *          The internal ARF model.
   * @param resourceModel
   *          The DiVA variability model resource.
   * @return The configuration model resource.
   */
  public Resource convert(ARFConfiguration arfConfiguration, ARFModel arfModel, Resource resourceModel, Resource resourceContext) {
    count++;
    Context context = null;
    EObject object = null;
    if (resourceModel != null) {
      object = resourceModel.getContents().get(0);
      if (object instanceof VariabilityModel) {
        VariabilityModel model = (VariabilityModel) object;
        Scenario scenario = ARFModeling.getReasoningScenario(model);
        if (scenario != null) {
          context = scenario.getContext().get(0);
        }
      }
    }
    Configuration configuration = convert(arfConfiguration, arfModel, context);
    Resource resourceConfiguration = getResource(configuration, resourceContext != null ? resourceContext.getURI() : (resourceModel != null ? resourceModel
        .getURI() : null));
    if (resourceConfiguration == null) {
      resourceConfiguration = resourceModel;
    }
    if (object instanceof VariabilityModel) {
      VariabilityModel model = (VariabilityModel) object;
      if (model.getSimulation().getScenario().size() == 1) {
        ARFModeling.removeReasoningScenario(model);
      }
    }
    return resourceConfiguration;
  }

  /**
   * Convert the given internal ARF configuration to a configuration.
   * 
   * @param arfConfiguration
   *          The internal ARF configuration.
   * @param arfModel
   *          The internal ARF model.
   * @param context
   *          The context model.
   * @return The configuration model.
   */
  public Configuration convert(ARFConfiguration arfConfiguration, ARFModel arfModel, Context context) {

    // create a new configuration
    Configuration configuration = DivaFactory.eINSTANCE.createConfiguration();

    // calculate property priorities
    calculatePriorities(arfConfiguration, arfModel);

    int totalScore = 0;
    Map<String, Integer> propertyScore = new HashMap<String, Integer>();

    // calculate the scores for each variant and property,
    // and the total score for the configuration
    for (Entry<String, Variant> entry : arfModel.getVariants().entrySet()) {
      if (ARFModeling.isSelected(arfConfiguration, arfModel, entry.getKey())) {
        String variantID = entry.getKey();
        Variant variant = entry.getValue();
        // create a new variant element
        ConfigVariant cv = DivaFactory.eINSTANCE.createConfigVariant();
        cv.setVariant(variant);
        totalScore += calculateForVariant(arfModel, cv, variantID, propertyScore);
        configuration.getVariant().add(cv);
      }
    }
    configuration.setTotalScore(totalScore);
    configuration.setVerdict(Verdict.PASS);

    // add the total scores for each property
    addPropertyScores(arfModel, configuration, propertyScore);

    if (context != null) {
      String oracleName = ARFModelConstants.ORACLE_PREFIX + context.getId();
      if (ARFModeling.getFeatureWithName(arfModel, oracleName) != null) {
        if (ARFModeling.isSelected(arfConfiguration, arfModel, oracleName) == false) {
          configuration.setVerdict(Verdict.FAIL);
        }
      }
      // clear the property priorities
      context.getPriority().clear();
      // add the property priorities
      for (Entry<String, Integer> entry : propertyPriority.entrySet()) {
        // create a new score element
        Priority p = DivaFactory.eINSTANCE.createPriority();
        p.setProperty(arfModel.getProperties().get(entry.getKey()));
        p.setPriority(entry.getValue());
        context.getPriority().add(p);
      }
      // add the configuration
      context.getConfiguration().add(configuration);
      // set resource modified
      context.eResource().setModified(true);
    }
    return configuration;
  }

  /**
   * Calculate the priority of each priority property. That is for a property
   * the maximum priority defined in any of the active rules.
   * 
   * @param arfConfiguration
   *          The ARF configuration model.
   * @param arfModel
   *          The ARF reasoning model.
   */
  private void calculatePriorities(ARFConfiguration arfConfiguration, ARFModel arfModel) {
    if (propertyPriority != null) {
      Set<String> rules = arfModel.getRules().keySet();
      for (String name : rules) {
        if (ARFModeling.isSelected(arfConfiguration, arfModel, name)) {
          // active rule
          for (String propertyID : arfModel.getProperties().keySet()) {
            // save the maximum priority for the current property
            int priority = arfModel.getPriority(name, propertyID);
            if (propertyPriority.containsKey(propertyID)) {
              int oldpriority = propertyPriority.get(propertyID);
              if (priority > oldpriority) {
                // update priority for this property
                propertyPriority.put(propertyID, priority);
              }
            }
            else {
              propertyPriority.put(propertyID, priority);
            }
          }
        }
      }
    }
  }

  /**
   * Calculate the score for each variant. Adds a corresponding variant element
   * containing the calculated scores.
   * 
   * @param arfModel
   *          The ARF model.
   * @param cv
   *          The configuration variant.
   * @param variantID
   *          The variant identifier.
   * @param propertyScore
   *          Map for all calculated property scores.
   * @return The total score of the variant.
   */
  private int calculateForVariant(ARFModel arfModel, ConfigVariant cv, String variantID, Map<String, Integer> propertyScore) {
    int totalScore = 0;

    // calculate the score for each property
    // and the total score of the variant element
    for (Entry<String, Property> entry : arfModel.getProperties().entrySet()) {
      String propertyID = entry.getKey();
      Property property = entry.getValue();
      // create a new score element
      Score s = DivaFactory.eINSTANCE.createScore();
      s.setProperty(property);
      int score = calculateForProperty(arfModel, s, variantID, propertyID);
      totalScore += score;

      // update the total property score
      if (propertyScore.containsKey(propertyID)) {
        int oldscore = propertyScore.get(propertyID);
        propertyScore.put(propertyID, oldscore + score);
      }
      else {
        propertyScore.put(propertyID, score);
      }

      cv.getScore().add(s);
    }

    // set total score and add the variant to the configuration
    cv.setTotalScore(totalScore);

    return totalScore;
  }

  /**
   * Calculate the score for a property. Also updates the total score of the
   * property in the configuration. Adds a corresponding score element to the
   * variant.
   * 
   * @param arfModel
   *          The ARF model.
   * @param s
   *          The score of a property.
   * @param variantID
   *          The variant identifier.
   * @param propertyID
   *          The property identifier.
   * @return The score of the property.
   */
  private int calculateForProperty(ARFModel arfModel, Score s, String variantID, String propertyID) {
    int score = 0;
    if (propertyPriority != null) {
      // calculate the score of the current property
      int priority = propertyPriority.containsKey(propertyID) == true ? propertyPriority.get(propertyID) : -1;
      int direction = arfModel.getDirection(propertyID);
      int value = arfModel.getValue(variantID, propertyID);
      score = (int) Math.pow(K, priority) * value;
      if (direction == 0) {
        score = -score;
      }
    }

    // set the score and the score element to the variant
    s.setScore(score);

    return score;
  }

  /**
   * Add the calculated total property scores.
   * 
   * @param arfModel
   *          The ARF model.
   * @param configuration
   *          The new configuration.
   * @param propertyScore
   *          The calculated scores for all properties.
   */
  private void addPropertyScores(ARFModel arfModel, Configuration configuration, Map<String, Integer> propertyScore) {
    for (Entry<String, Integer> entry : propertyScore.entrySet()) {
      // create a new score element
      Score s = DivaFactory.eINSTANCE.createScore();
      s.setProperty(arfModel.getProperties().get(entry.getKey()));
      s.setScore(entry.getValue());
      configuration.getScore().add(s);
    }
  }

}
