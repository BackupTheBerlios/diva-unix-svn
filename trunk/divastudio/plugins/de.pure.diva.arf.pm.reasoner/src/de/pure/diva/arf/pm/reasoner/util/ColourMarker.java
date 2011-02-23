package de.pure.diva.arf.pm.reasoner.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.featuremodel.Constraint;
import org.eclipse.featuremodel.Feature;
import org.eclipse.featuremodel.FeatureModelFactory;
import org.eclipse.featuremodel.Group;
import org.eclipse.variantmodel.VariantSelection;

import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.IDGenerator;
import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;

public class ColourMarker {

  private ARFConstraintAnalyser analyser = new ARFConstraintAnalyser();

  public final static String    WHITE    = "white";

  private String getColour() {
    String colour = IDGenerator.generate();
    colour = colour.replaceAll("-", "__");
    return colour;
  }

  public Map<String, ARFModel> mark(ARFModel arfModel, ARFContext arfContext) {
    Map<String, ARFModel> colour2modelMap = new HashMap<String, ARFModel>();
    Map<String, Set<Feature>> colour2featuresMap = new HashMap<String, Set<Feature>>();
    Map<Feature, String> feature2colourMap = new HashMap<Feature, String>();
    Map<String, Feature> name2featureMap = new HashMap<String, Feature>();
    Set<Feature> fixedFeatures = getFixFeatures(arfContext);

    mark(WHITE, arfModel.getRoot(), colour2featuresMap, feature2colourMap, fixedFeatures, name2featureMap);
    Map<String, Set<Constraint>> constraints = merge(arfModel, colour2featuresMap, feature2colourMap, name2featureMap);

    ARFModel whiteModel = createWhiteModel(arfModel, feature2colourMap);
    // colour2modelMap.put(WHITE, whiteModel);
    for (String colour : colour2featuresMap.keySet()) {
      if (WHITE.equals(colour) == false) {
        ARFModel colourModel = createColourModel(whiteModel, colour, colour2featuresMap, feature2colourMap, constraints);
        colour2modelMap.put(colour, colourModel);
      }
    }
    return colour2modelMap;
  }

  protected void print(Feature feature, String ident) {
    System.out.println(ident + feature.getName());
    Map<String, List<Feature>> children = ARFModeling.getChildren(feature);
    List<Feature> mandatories = children.get(ARFModelConstants.MANDATORY);
    List<Feature> optionals = children.get(ARFModelConstants.OPTIONAL);
    List<Feature> alternatives = children.get(ARFModelConstants.ALTERNATIVE);
    List<Feature> ors = children.get(ARFModelConstants.OR);

    for (Feature child : mandatories) {
      print(child, "MA:" + ident + "\t");
    }
    for (Feature child : optionals) {
      print(child, "OP:" + ident + "\t");
    }
    for (Feature child : alternatives) {
      print(child, "AL:" + ident + "\t");
    }
    for (Feature child : ors) {
      print(child, "OR:" + ident + "\t");
    }
  }

  private ARFModel createColourModel(ARFModel whiteModel, String colour, Map<String, Set<Feature>> colour2featuresMap, Map<Feature, String> feature2colourMap,
      Map<String, Set<Constraint>> constraints) {
    Set<String> addedColouredConstraints = new HashSet<String>();
    ARFModel colourModel = new ARFModel(whiteModel);
    Feature colourRoot = ARFModeling.createFeature(colour, colour, "diva:colour");
    ARFModeling.addChild(colourModel.getRoot(), colourRoot, -1, -1);
    for (Feature parent : colour2featuresMap.get(colour)) {
      Feature colourParent = colourRoot;
      Feature newParent = createFeature(colourModel, colourParent, parent, constraints, addedColouredConstraints);
      createColourModel(colourModel, colour, newParent, parent, feature2colourMap, constraints, addedColouredConstraints);
    }
    return colourModel;
  }

  private ARFModel createWhiteModel(ARFModel arfModel, Map<Feature, String> feature2colourMap) {
    ARFModel whiteModel = new ARFModel();
    whiteModel.setId(WHITE);
    Feature newRoot = ARFModeling.createFeature(arfModel.getRoot().getName(), arfModel.getRoot().getName(), arfModel.getRoot().getType());
    newRoot.setId(arfModel.getRoot().getId());
    whiteModel.setRoot(newRoot);
    createColourModel(whiteModel, WHITE, newRoot, arfModel.getRoot(), feature2colourMap, new HashMap<String, Set<Constraint>>(), new HashSet<String>());
    return whiteModel;
  }

  private void createColourModel(ARFModel colourModel, String colour, Feature newParent, Feature parent, Map<Feature, String> feature2colourMap,
      Map<String, Set<Constraint>> constraints, Set<String> addedColouredConstraints) {
    for (Group group : parent.getChildren()) {
      for (Feature child : group.getFeatures()) {
        String c = feature2colourMap.get(child);
        if (colour.equals(c) == true) {
          Feature newChild = createFeature(colourModel, newParent, child, constraints, addedColouredConstraints);
          createColourModel(colourModel, colour, newChild, child, feature2colourMap, constraints, addedColouredConstraints);
        }
      }
    }

  }

  private Feature createFeature(ARFModel model, Feature parent, Feature original, Map<String, Set<Constraint>> constraints, Set<String> addedConstraints) {
    Feature feature = ARFModeling.createFeature(original.getName(), original.getName(), original.getType());
    ARFModeling.addChild(parent, feature, ARFModeling.getParentGroup(original).getLower(), ARFModeling.getParentGroup(original).getUpper());
    Set<Constraint> constraintOfItem = constraints.get(feature.getName());
    if (constraintOfItem != null) {
      for (Constraint constraint : constraintOfItem) {
        if (addedConstraints.contains(constraint.getId()) == false) {
          Constraint c = FeatureModelFactory.eINSTANCE.createConstraint();
          c.setId(constraint.getId());
          c.setLanguage(constraint.getLanguage());
          c.setCode(constraint.getCode());
          model.getConstraints().add(c);
          addedConstraints.add(constraint.getId());
        }
      }
    }
    return feature;
  }

  private boolean isOptional(Feature feature, Map<Feature, String> feature2colourMap) {
    boolean result = false;
    Feature parent = ARFModeling.getParent(feature);
    if (parent != null && WHITE.equals(feature2colourMap.get(parent)) == false) {
      Group group = ARFModeling.getParentGroup(parent);
      if (group.getLower() == 0) {
        result = true;
      }
      else {
        result = isOptional(parent, feature2colourMap);
      }
    }
    return result;
  }

  private Map<String, Set<Constraint>> merge(ARFModel model, Map<String, Set<Feature>> colour2featuresMap, Map<Feature, String> feature2colourMap,
      Map<String, Feature> name2featureMap) {
    Map<String, Set<String>> dependencies = new HashMap<String, Set<String>>();
    Map<String, Set<Constraint>> constraints = new HashMap<String, Set<Constraint>>();
    fillDependencyMatrix(model, dependencies, constraints);

    for (String name : dependencies.keySet()) {
      Feature feature = name2featureMap.get(name);
      if (feature != null) {
        String colour = feature2colourMap.get(feature);
        if (WHITE.equals(colour) == false) {
          for (String item : dependencies.get(name)) {
            Feature dependendFeature = name2featureMap.get(item);
            if (dependendFeature != null) {
              String dependendColour = feature2colourMap.get(dependendFeature);
              if (colour.equals(dependendColour) == false && WHITE.equals(dependendColour) == false) {
                Set<Feature> dependendParentFeatures = colour2featuresMap.get(dependendColour);
                for (Feature f : dependendParentFeatures) {
                  feature2colourMap.put(f, colour);
                  merge(f, dependendColour, colour, feature2colourMap);
                }
                colour2featuresMap.get(colour).addAll(dependendParentFeatures);
                colour2featuresMap.remove(dependendColour);
              }
            }
          }
        }
      }
    }

    return constraints;
  }

  private void merge(Feature parent, String dependendColour, String colour, Map<Feature, String> feature2colourMap) {
    for (Group group : parent.getChildren()) {
      for (Feature feature : group.getFeatures()) {
        String c = feature2colourMap.get(feature);
        if (dependendColour.equals(c) == true) {
          feature2colourMap.put(feature, colour);
          merge(feature, dependendColour, colour, feature2colourMap);
        }
      }
    }
  }

  private void mark(String colour, Feature parent, Map<String, Set<Feature>> colour2featuresMap, Map<Feature, String> feature2colourMap,
      Set<Feature> fixedFeatures, Map<String, Feature> name2featureMap) {
    Set<Feature> features = colour2featuresMap.get(colour);
    if (features == null) {
      features = new HashSet<Feature>();
      colour2featuresMap.put(colour, features);
    }
    if (colour.equals(feature2colourMap.get(ARFModeling.getParent(parent))) == false) {
      features.add(parent);
    }
    name2featureMap.put(parent.getName(), parent);
    feature2colourMap.put(parent, colour);

    Map<String, List<Feature>> children = ARFModeling.getChildren(parent);
    List<Feature> mandatories = children.get(ARFModelConstants.MANDATORY);
    List<Feature> optionals = children.get(ARFModelConstants.OPTIONAL);
    List<Feature> alternatives = children.get(ARFModelConstants.ALTERNATIVE);
    List<Feature> ors = children.get(ARFModelConstants.OR);

    for (Feature feature : mandatories) {
      if (fixedFeatures.contains(feature) == true || isOptional(feature, feature2colourMap) == true) {
        mark(colour, feature, colour2featuresMap, feature2colourMap, fixedFeatures, name2featureMap);
      }
      else {
        String newColour = getColour();
        mark(newColour, feature, colour2featuresMap, feature2colourMap, fixedFeatures, name2featureMap);
      }
    }

    for (Feature feature : optionals) {
      if (fixedFeatures.contains(feature) == true) {
        mark(colour, feature, colour2featuresMap, feature2colourMap, fixedFeatures, name2featureMap);
      }
      else {
        String newColour = getColour();
        mark(newColour, feature, colour2featuresMap, feature2colourMap, fixedFeatures, name2featureMap);
      }
    }

    for (Feature feature : alternatives) {
      mark(colour, feature, colour2featuresMap, feature2colourMap, fixedFeatures, name2featureMap);
    }

    for (Feature feature : ors) {
      mark(colour, feature, colour2featuresMap, feature2colourMap, fixedFeatures, name2featureMap);
    }
  }

  private Set<Feature> getFixFeatures(ARFContext context) {
    Set<Feature> fixed = new HashSet<Feature>();
    EList<VariantSelection> selections = context.getSelections();
    for (VariantSelection selection : selections) {
      Feature feature = selection.getFeature();
      if (feature != null) {
        fixed.add(feature);
        collectFixedParents(feature, fixed);
      }
    }
    return fixed;
  }

  private void collectFixedParents(Feature feature, Set<Feature> fixed) {
    Feature parent = ARFModeling.getParent(feature);
    if (parent != null && fixed.contains(parent) == false) {
      fixed.add(parent);
      collectFixedParents(parent, fixed);
    }
  }

  private void fillDependencyMatrix(ARFModel model, Map<String, Set<String>> dependencies, Map<String, Set<Constraint>> constraints) {
    for (Constraint constraint : model.getConstraints()) {
      String code = constraint.getCode();
      Set<String> items = analyser.getItems(code);
      for (String item : items) {
        Set<String> dependencyOfItem = dependencies.get(item);
        if (dependencyOfItem == null) {
          dependencyOfItem = new HashSet<String>();
          dependencies.put(item, dependencyOfItem);
        }
        dependencyOfItem.addAll(items);
        dependencyOfItem.remove(item);
        Set<Constraint> constraintOfItem = constraints.get(item);
        if (constraintOfItem == null) {
          constraintOfItem = new HashSet<Constraint>();
          constraints.put(item, constraintOfItem);
        }
        constraintOfItem.add(constraint);
      }
    }
  }

}
