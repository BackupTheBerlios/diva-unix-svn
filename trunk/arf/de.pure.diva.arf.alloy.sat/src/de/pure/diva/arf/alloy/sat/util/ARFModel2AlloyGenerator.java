package de.pure.diva.arf.alloy.sat.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.featuremodel.Constraint;
import org.eclipse.featuremodel.Feature;
import org.eclipse.variantmodel.SelectionState;
import org.eclipse.variantmodel.VariantSelection;

import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;
import arf.model.util.ARFModeling.Range;

public class ARFModel2AlloyGenerator {
  private static final String SOLUTION_DESCRIPTION = "ALLOY___SOLUTION___";
  private static final String ABSTRACT_ELEMENT     = "ALLOY___ABSTRACT___ELEMENT___";
  private static final String IMAGINARY_ROOT       = "ALLOY___IMAGINARY___ROOT___";
  private static final String RANGEGROUP_SUFFIX    = "ALLOY___RANGEGROUP";

  private ARFConfiguration    configuration        = null;

  private StringBuffer        sigs                 = new StringBuffer();
  private StringBuffer        facts                = new StringBuffer();

  public ARFModel2AlloyGenerator() {}

  public ARFModel2AlloyGenerator(ARFConfiguration configuration) {
    this.configuration = configuration;
  }

  private boolean isSelected(ARFModel model, Feature element) {
    boolean result = false;
    if (this.configuration != null) {
      String name = element.getName();
      result = ARFModeling.isSelected(this.configuration, model, name);
    }
    return result;
  }

  private boolean isExcluded(ARFModel model, Feature element) {
    boolean result = false;
    if (this.configuration != null) {
      String name = element.getName();
      result = ARFModeling.isExcluded(this.configuration, model, name);
    }
    return result;
  }

  public String generate(ARFModel model) {
    StringBuffer buffer = new StringBuffer();
    String solution = generateSolution(buffer, ABSTRACT_ELEMENT);
    generateAbstractElement(buffer, ABSTRACT_ELEMENT, solution);
    String parent = generateImaginaryRoot(buffer, ABSTRACT_ELEMENT);
    Feature element = model.getRoot();
    if (element != null) {
      this.sigs.append("\n/* ********************\n");
      this.sigs.append(" * Element Tree Definition\n");
      this.sigs.append(" **********************/\n");
      List<Feature> elements = new ArrayList<Feature>();
      elements.add(element);
      generateMandatories(model, elements, parent, ABSTRACT_ELEMENT, true);
    }
    ARFConstraint2AlloyFactConverter converter = new ARFConstraint2AlloyFactConverter();
    for (Constraint constraint : model.getConstraints()) {
      String code = constraint.getCode();
      this.facts.append("fact { ");
      this.facts.append(converter.convert(code));
      this.facts.append(" }\n");
    }
    buffer.append(this.sigs);
    buffer.append(this.facts);
    return buffer.toString();
  }

  private String generateSolution(StringBuffer buffer, String base) {
    buffer.append("\n/* ********************\n");
    buffer.append(" * Solution Description\n");
    buffer.append(" **********************/\n");
    buffer.append("one sig ");
    buffer.append(SOLUTION_DESCRIPTION);
    buffer.append(" {\n\telements : set ");
    buffer.append(base);
    buffer.append("\n}\n");
    return SOLUTION_DESCRIPTION;
  }

  private void generateAbstractElement(StringBuffer buffer, String element, String solution) {
    buffer.append("\n/* ********************\n");
    buffer.append(" * Abstract Element Definition\n");
    buffer.append(" **********************/\n");
    buffer.append("abstract sig ");
    buffer.append(element);
    buffer.append(" {}\n");
    buffer.append("fact { one s: ");
    buffer.append(solution);
    buffer.append(" | all elem: ");
    buffer.append(element);
    buffer.append(" | elem in s.elements }\n");
  }

  private String generateImaginaryRoot(StringBuffer buffer, String base) {
    buffer.append("\n/* ********************\n");
    buffer.append(" * Imaginary Root Element\n");
    buffer.append(" **********************/\n");
    buffer.append("one sig ");
    buffer.append(IMAGINARY_ROOT);
    buffer.append(" extends ");
    buffer.append(base);
    buffer.append(" {}\n");
    return IMAGINARY_ROOT;
  }

  private void generateMandatories(ARFModel model, List<Feature> elements, String parent, String base, boolean mandatory) {
    if (elements.isEmpty() == false) {
      generateChildren(model, elements, ARFModeling.getRange("[n,n]"), parent, base, mandatory);
    }
  }

  private void generateOptionals(ARFModel model, List<Feature> elements, String parent, String base, boolean mandatory) {
    if (elements.isEmpty() == false) {
      generateChildren(model, elements, ARFModeling.getRange("[0,n]"), parent, base, mandatory);
    }
  }

  private void generateAlternatives(ARFModel model, List<Feature> elements, String parent, String base, boolean mandatory) {
    if (elements.isEmpty() == false) {
      generateChildren(model, elements, ARFModeling.getRange("[1,1]"), parent, base, mandatory);
    }
  }

  private void generateOrs(ARFModel model, List<Feature> elements, Range range, String parent, String base, boolean mandatory) {
    if (elements.isEmpty() == false) {
      generateChildren(model, elements, range, parent, base, mandatory);
    }
  }

  private void generateChildren(ARFModel model, List<Feature> elements, Range range, String parent, String base, boolean mandatory) {
    String childrenBase = generateAbstractGroupBase(model, elements, range, base, mandatory);
    generateParentFact(elements, parent);
    for (Feature element : elements) {
      boolean lone = true;
      if (isSelected(model, element) == true || (range.lower == Range.UNBOUNDED && mandatory == true)) {
        this.sigs.append("one");
        lone = false;
      }
      else if (isExcluded(model, element) == true) {
        this.sigs.append("lone abstract");
      }
      else {
        this.sigs.append("lone");
      }
      this.sigs.append(" sig ");
      this.sigs.append(element.getName());
      this.sigs.append(" extends ");
      this.sigs.append(childrenBase);
      this.sigs.append(" {}\n");
      if (isExcluded(model, element) == true) {
        generateExcludeFact(element);
      }
      generateChildren(model, element, base, lone == false);
    }
    if (range.lower != 0) {
      if (range.lower == Range.UNBOUNDED) {
        generateChildrenFact(elements, "and", parent);
      }
      else {
        generateChildrenFact(elements, "or", parent);
      }
    }
  }

  private String generateAbstractGroupBase(ARFModel model, List<Feature> elements, Range range, String base, boolean mandatory) {
    String childrenBase = base;
    if (range.upper == 1) {
      StringBuffer baseRange = new StringBuffer();
      boolean selected = false;
      for (Feature element : elements) {
        if (isSelected(model, element) == true) {
          selected = true;
        }
        baseRange.append(element.getName());
      }
      baseRange.append(RANGEGROUP_SUFFIX);
      childrenBase = baseRange.toString();
      if (selected == true || mandatory == true) {
        this.sigs.append("one");
      }
      else {
        this.sigs.append("lone");
      }
      this.sigs.append(" abstract sig ");
      this.sigs.append(childrenBase);
      this.sigs.append(" extends ");
      this.sigs.append(base);
      this.sigs.append(" {}\n");
    }
    return childrenBase;
  }

  private void generateParentFact(List<Feature> elements, String parent) {
    this.facts.append("fact { (");
    for (Iterator<Feature> element = elements.iterator(); element.hasNext() == true;) {
      this.facts.append(" one ");
      this.facts.append(element.next().getName());
      if (element.hasNext() == true) {
        this.facts.append(" or");
      }
    }
    this.facts.append(" ) implies one ");
    this.facts.append(parent);
    this.facts.append(" }\n");
  }

  private void generateChildrenFact(List<Feature> elements, String operation, String parent) {
    this.facts.append("fact { one ");
    this.facts.append(parent);
    this.facts.append(" implies (");
    for (Iterator<Feature> element = elements.iterator(); element.hasNext() == true;) {
      this.facts.append(" one ");
      this.facts.append(element.next().getName());
      if (element.hasNext() == true) {
        this.facts.append(" ");
        this.facts.append(operation);
      }
    }
    this.facts.append(" ) }\n");
  }

  private void generateExcludeFact(Feature element) {
    this.facts.append("fact { one ");
    this.facts.append(element.getName());
    this.facts.append(" implies no ");
    this.facts.append(element.getName());
    this.facts.append(" }\n");
  }

  private void generateChildren(ARFModel model, Feature parent, String base, boolean mandatory) {
    Map<String, List<Feature>> children = ARFModeling.getChildren(parent);
    Range range = ARFModeling.getRange(parent);
    List<Feature> mandatories = children.get(ARFModelConstants.MANDATORY);
    generateMandatories(model, mandatories, parent.getName(), base, mandatory);
    List<Feature> optionals = children.get(ARFModelConstants.OPTIONAL);
    generateOptionals(model, optionals, parent.getName(), base, mandatory);
    List<Feature> alternatives = children.get(ARFModelConstants.ALTERNATIVE);
    generateAlternatives(model, alternatives, parent.getName(), base, mandatory);
    List<Feature> ors = children.get(ARFModelConstants.OR);
    generateOrs(model, ors, range, parent.getName(), base, mandatory);
    List<Feature> unknowns = children.get("");
    unknowns.clear();
  }

  public String select(ARFModel model, ARFContext context) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("one ");
    buffer.append(IMAGINARY_ROOT);
    if (context != null) {
      selectContext(buffer, context);
    }
    if (model != null) {
      Feature element = model.getRoot();
      if (element != null) {
        List<Feature> list = new ArrayList<Feature>();
        list.add(element);
        selectMandatories(buffer, list);
      }
    }
    return buffer.toString();
  }

  private void selectContext(StringBuffer buffer, ARFContext context) {
    List<VariantSelection> selections = context.getSelections();
    selectSelections(buffer, selections);
  }

  public String select(ARFModel model, ARFConfiguration configuration) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("one ");
    buffer.append(IMAGINARY_ROOT);
    if (configuration != null) {
      selectConfiguration(buffer, configuration);
    }
    if (model != null && this.configuration != configuration) {
      Feature element = model.getRoot();
      if (element != null) {
        List<Feature> list = new ArrayList<Feature>();
        list.add(element);
        selectMandatories(buffer, list);
      }
    }
    return buffer.toString();
  }

  private void selectConfiguration(StringBuffer buffer, ARFConfiguration configuration) {
    List<VariantSelection> elements = configuration.getSelections();
    List<VariantSelection> selections = elements;
    selectSelections(buffer, selections);
  }

  private void selectSelections(StringBuffer buffer, List<VariantSelection> selections) {
    for (VariantSelection selection : selections) {
      buffer.append(" and ");
      String name = ARFModeling.getElementName(selection);
      if (SelectionState.SELECTED.getLiteral().equals(selection.getState().getLiteral()) == true) {
        buffer.append("one " + name);
      }
      else if (SelectionState.EXCLUDED.getLiteral().equals(selection.getState().getLiteral()) == true) {
        buffer.append("no " + name);
      }
      else {
        buffer.append("no " + name);
      }
    }
  }

  private void selectMandatories(StringBuffer buffer, List<Feature> children) {
    for (Feature element : children) {
      if (ARFModelConstants.ORACLES_GROUP_UNAME.equals(element.getName()) == false) {
        buffer.append(" and one ");
        buffer.append(element.getName());
        selectMandatories(buffer, ARFModeling.getChildren(element).get(ARFModelConstants.MANDATORY));
      }
    }
  }
}
