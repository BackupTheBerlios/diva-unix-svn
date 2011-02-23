package arf.model.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;

import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.util.mapper.ARFConfiguration2ConfigurationModel;
import arf.model.util.mapper.ConfigurationModel2ARFConfiguration;
import arf.model.util.mapper.ContextModel2ARFContext;
import arf.model.util.mapper.VariabilityModel2ARFModel;

public class Mapper implements service.arf.model.Mapper {

  private Map<Resource, ARFModel>         models            = new HashMap<Resource, ARFModel>();
  private Map<Resource, ARFContext>       contexts          = new HashMap<Resource, ARFContext>();
  private Map<Resource, ARFConfiguration> configurations    = new HashMap<Resource, ARFConfiguration>();
  private Map<ARFModel, Resource>         arfModels         = new HashMap<ARFModel, Resource>();
  private Map<ARFContext, Resource>       arfContexts       = new HashMap<ARFContext, Resource>();
  private Map<ARFConfiguration, Resource> arfConfigurations = new HashMap<ARFConfiguration, Resource>();

  public ARFConfiguration mapConfiguration(Resource model, Resource context, Resource configuration) {
    ARFConfiguration arfConfiguration = configurations.get(configuration);
    if (arfConfiguration == null) {
      ARFModel arfModel = mapModel(model);
      ARFContext arfContext = mapContext(model, context);
      ConfigurationModel2ARFConfiguration converter = new ConfigurationModel2ARFConfiguration();
      arfConfiguration = converter.convert(configuration, model, arfModel, arfContext);
      configurations.put(configuration, arfConfiguration);
      arfConfigurations.put(arfConfiguration, configuration);
    }
    return arfConfiguration;
  }

  public Resource mapConfiguration(ARFModel model, ARFContext context, ARFConfiguration configuration) {
    Resource divaConfiguration = arfConfigurations.get(configuration);
    if (divaConfiguration == null) {
      Resource divaModel = mapModel(model);
      Resource contextModel = mapContext(model, context);
      Map<String, Integer> propertyPriority = new HashMap<String, Integer>();
      ARFConfiguration2ConfigurationModel converter = new ARFConfiguration2ConfigurationModel(propertyPriority);
      divaConfiguration = converter.convert(configuration, model, divaModel, contextModel);
      arfConfigurations.put(configuration, divaConfiguration);
      configurations.put(divaConfiguration, configuration);
    }
    return divaConfiguration;
  }

  public ARFContext mapContext(Resource model, Resource context) {
    ARFModel arfModel = mapModel(model);
    ARFContext arfContext = contexts.get(context);
    if (arfContext == null) {
      ContextModel2ARFContext converter = new ContextModel2ARFContext();
      arfContext = converter.convert(context, model, arfModel);
      contexts.put(context, arfContext);
      arfContexts.put(arfContext, context);
    }
    return arfContext;
  }

  public Resource mapContext(ARFModel model, ARFContext context) {
    // VariabilityModel wp2Model = mapModel(model);
    Resource divaContext = arfContexts.get(context);
    if (divaContext == null) {
      // TODO Auto-generated method stub
    }
    return divaContext;
  }

  public ARFModel mapModel(Resource model) {
    ARFModel arfModel = models.get(model);
    if (arfModel == null) {
      VariabilityModel2ARFModel converter = new VariabilityModel2ARFModel();
      arfModel = converter.convert(model);
      models.put(model, arfModel);
      arfModels.put(arfModel, model);
    }
    return arfModel;
  }

  public Resource mapModel(ARFModel model) {
    Resource divaModel = arfModels.get(model);
    if (divaModel == null) {
      // TODO Auto-generated method stub
    }
    return divaModel;
  }

}
