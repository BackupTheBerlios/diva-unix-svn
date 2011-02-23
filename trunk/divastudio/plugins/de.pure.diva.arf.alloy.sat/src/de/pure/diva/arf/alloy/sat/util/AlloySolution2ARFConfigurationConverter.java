package de.pure.diva.arf.alloy.sat.util;

import java.util.List;

import org.eclipse.featuremodel.Feature;
import org.eclipse.featuremodel.Group;
import org.eclipse.variantmodel.SelectionState;

import arf.model.ARFConfiguration;
import arf.model.ARFModel;
import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;

public class AlloySolution2ARFConfigurationConverter {

  public ARFConfiguration convert(List<String> solution, ARFModel model) {
    ARFConfiguration configuration = new ARFConfiguration();
    ARFModeling.setModelName(configuration, ARFModelConstants.CONFIGURATION_NAME);
    return convert(solution, configuration, model);
  }

  public ARFConfiguration convert(List<String> solution, ARFConfiguration configuration, ARFModel model) {
    selectAtoms(solution, configuration, model);
    excludeOthers(configuration, model);
    return configuration;
  }

  private void selectAtoms(List<String> atoms, ARFConfiguration configuration, ARFModel model) {
    for (String atom : atoms) {
      Feature element = ARFModeling.getFeatureWithName(model, atom);
      if (element != null) {
        selectElement(configuration, model, element);
        selectParent(configuration, model, element);
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
