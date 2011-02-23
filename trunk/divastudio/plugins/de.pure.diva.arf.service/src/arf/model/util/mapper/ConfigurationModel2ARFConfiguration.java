package arf.model.util.mapper;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.featuremodel.Feature;
import org.eclipse.featuremodel.Group;
import org.eclipse.variantmodel.SelectionState;
import org.eclipse.variantmodel.VariantSelection;

import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.ARFVariant;
import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;
import diva.ConfigVariant;
import diva.Configuration;
import diva.Context;
import diva.Dimension;
import diva.DivaFactory;
import diva.Scenario;
import diva.VariabilityModel;
import diva.Variant;

public class ConfigurationModel2ARFConfiguration {
  private diva_configuration.Aspect getAspect(diva_configuration.Configuration configuration, Variant variant) {
    diva_configuration.Aspect aspect = null;
    String name = variant.getName();
    EList<diva_configuration.Aspect> aspects = configuration.getAspect();
    for (diva_configuration.Aspect tmp : aspects) {
      if (tmp.getName().equals(name) == true) {
        aspect = tmp;
        break;
      }
    }
    return aspect;
  }

  public ARFConfiguration convert(Resource resourceConfiguration, Resource resourceModel, ARFModel arfModel, ARFContext arfContext) {
    Configuration configuration = null;
    EObject objectModel = resourceModel.getContents().get(0);
    EObject objectConfiguration = resourceConfiguration.getContents().get(0);
    if (objectModel instanceof VariabilityModel) {
      VariabilityModel model = (VariabilityModel) objectModel;
      if (objectConfiguration instanceof diva_configuration.SuitableConfigurations) {
        diva_configuration.SuitableConfigurations configurationModel = (diva_configuration.SuitableConfigurations) objectConfiguration;
        diva_configuration.Configuration suitableConfiguration = configurationModel.getConfigurations().get(0);
        if (suitableConfiguration != null) {
          configuration = DivaFactory.eINSTANCE.createConfiguration();
          configuration.setTotalScore(suitableConfiguration.getScore());
          EList<Dimension> dimensions = model.getDimension();
          for (Dimension dimension : dimensions) {
            EList<Variant> variants = dimension.getVariant();
            for (Variant variant : variants) {
              diva_configuration.Aspect aspect = getAspect(suitableConfiguration, variant);
              if (aspect != null) {
                ConfigVariant cVariant = DivaFactory.eINSTANCE.createConfigVariant();
                cVariant.setVariant(variant);
                configuration.getVariant().add(cVariant);
              }
            }
          }
        }
      }
      else if (objectConfiguration instanceof VariabilityModel) {
        model = (VariabilityModel) objectConfiguration;
        Scenario scenario = ARFModeling.getReasoningScenario(model);
        if (scenario != null) {
          Context context = scenario.getContext().get(0);
          if (context != null) {
            configuration = context.getConfiguration().get(0);
          }
        }
      }
    }

    return convert(configuration, arfModel, arfContext);
  }

  public ARFConfiguration convert(Configuration configuration, ARFModel arfModel, ARFContext arfContext) {
    ARFConfiguration arfConfiguration = new ARFConfiguration();
    ARFModeling.setModelName(arfConfiguration, ARFModelConstants.CONFIGURATION_NAME);
    if (configuration != null) {
      copyARFContext(arfConfiguration, arfContext);
      selectConfiguration(arfConfiguration, configuration, arfModel);
      excludeNoConfigurationVariants(arfConfiguration, arfModel);
    }
    else {
      ARFModeling.setModelName(arfConfiguration, "NoConfiguration");
    }
    return arfConfiguration;
  }

  private void copyARFContext(ARFConfiguration arfConfiguration, ARFContext arfContext) {
    EList<VariantSelection> selections = arfContext.getSelections();
    for (VariantSelection selection : selections) {
      VariantSelection s = ARFVariant.copy(selection);
      arfConfiguration.getSelections().add(s);
    }
  }

  private void selectConfiguration(ARFConfiguration arfConfiguration, Configuration configuration, ARFModel arfModel) {
    List<ConfigVariant> variants = configuration.getVariant();
    for (ConfigVariant variant : variants) {
      if (variant.getVariant() != null) {
        String uname = ARFModelConstants.VARIANT_PREFIX + ARFModeling.getId(variant.getVariant());
        ARFModeling.addSelection(arfConfiguration, uname, arfModel, SelectionState.SELECTED);
      }
    }
  }

  private void excludeNoConfigurationVariants(ARFConfiguration arfConfiguration, ARFModel arfModel) {
    Feature root = arfModel.getRoot();
    excludeNoConfigurationVariants(arfConfiguration, arfModel, root);
  }

  private void excludeNoConfigurationVariants(ARFConfiguration arfConfiguration, ARFModel arfModel, Feature feature) {
    String name = feature.getName();
    if (ARFModelConstants.VARIANT_ELEMENT_TYPE.equals(feature.getType()) == true) {
      if (ARFModeling.isSelection(arfConfiguration, arfModel, name) == false) {
        ARFModeling.addSelection(arfConfiguration, name, arfModel, SelectionState.EXCLUDED);
      }
    }
    for (Group children : feature.getChildren()) {
      for (Feature child : children.getFeatures()) {
        excludeNoConfigurationVariants(arfConfiguration, arfModel, child);
      }
    }
  }
}
