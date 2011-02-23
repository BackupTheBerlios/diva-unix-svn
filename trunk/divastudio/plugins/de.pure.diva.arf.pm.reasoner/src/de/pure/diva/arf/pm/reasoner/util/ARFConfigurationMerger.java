package de.pure.diva.arf.pm.reasoner.util;

import java.util.List;

import org.eclipse.featuremodel.Feature;
import org.eclipse.featuremodel.Group;
import org.eclipse.variantmodel.SelectionState;
import org.eclipse.variantmodel.VariantSelection;

import arf.model.ARFConfiguration;
import arf.model.ARFModel;
import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;

public class ARFConfigurationMerger {

  public ARFConfiguration convert(List<ARFConfiguration> configurations, ARFModel model) {
    ARFConfiguration configuration = new ARFConfiguration();
    ARFModeling.setModelName(configuration, ARFModelConstants.CONFIGURATION_NAME);
    return convert(configurations, configuration, model);
  }

  public ARFConfiguration convert(List<ARFConfiguration> configurations, ARFConfiguration configuration, ARFModel model) {
    selectConfigurations(configurations, configuration, model);
    excludeOthers(configuration, model);
    return configuration;
  }

  private void selectConfigurations(List<ARFConfiguration> configurations, ARFConfiguration configuration, ARFModel model) {
    for (ARFConfiguration c : configurations) {
      for (VariantSelection selection : c.getSelections()) {
        boolean select = selection.getState().getLiteral().equals(SelectionState.SELECTED.getLiteral());
        Feature feature = selection.getFeature();
        if (feature != null) {
          String name = feature.getName();
          Feature element = ARFModeling.getFeatureWithName(model, name);
          if (element != null) {
            if (select == true) {
              selectElement(configuration, model, element);
              selectParent(configuration, model, element);
            }
            else {
              excludeElement(configuration, model, element);
            }
          }
        }
      }
    }
  }

  private void selectParent(ARFConfiguration configuration, ARFModel model, Feature element) {
    Feature parent = ARFModeling.getParent(element);
    if (parent != null) {
      selectElement(configuration, model, parent);
      selectParent(configuration, model, parent);
    }
  }

  private void selectElement(ARFConfiguration configuration, ARFModel model, Feature element) {
    if (ARFModeling.isSelection(configuration, model, element.getName()) == false) {
      ARFModeling.addSelection(configuration, element.getName(), model, SelectionState.SELECTED);
    }
  }

  private void excludeOthers(ARFConfiguration configuration, ARFModel model) {
    Feature root = model.getRoot();
    excludeOthers(configuration, model, root);
  }

  private void excludeOthers(ARFConfiguration configuration, ARFModel model, Feature element) {
    if (ARFModeling.isSelected(configuration, model, element.getName()) == false) {
      excludeElement(configuration, model, element);
    }
    for (Group group : element.getChildren()) {
      for (Feature child : group.getFeatures()) {
        excludeOthers(configuration, model, child);
      }
    }
  }

  private void excludeElement(ARFConfiguration configuration, ARFModel model, Feature element) {
    if (ARFModeling.isSelection(configuration, model, element.getName()) == false) {
      ARFModeling.addSelection(configuration, element.getName(), model, SelectionState.EXCLUDED);
    }
  }

}
