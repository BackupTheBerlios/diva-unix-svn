package arf.model;

import org.eclipse.featuremodel.Attribute;
import org.eclipse.featuremodel.AttributeType;
import org.eclipse.featuremodel.AttributeTypeBoolean;
import org.eclipse.featuremodel.AttributeTypeEObject;
import org.eclipse.featuremodel.AttributeTypeInt;
import org.eclipse.featuremodel.AttributeTypeString;
import org.eclipse.featuremodel.AttributeValue;
import org.eclipse.featuremodel.AttributeValueBoolean;
import org.eclipse.featuremodel.AttributeValueEObject;
import org.eclipse.featuremodel.AttributeValueInt;
import org.eclipse.featuremodel.AttributeValueString;
import org.eclipse.featuremodel.Description;
import org.eclipse.featuremodel.FeatureModelFactory;
import org.eclipse.variantmodel.VariantModel;
import org.eclipse.variantmodel.VariantModelFactory;
import org.eclipse.variantmodel.VariantSelection;
import org.eclipse.variantmodel.VariantValue;
import org.eclipse.variantmodel.impl.VariantModelImpl;

/**
 * The ARFVariant represents an abstract internal context or configuration model
 * of the framework. It is implemented as an EMF Variant Model.
 * 
 * @see VariantModel
 */
public abstract class ARFVariant extends VariantModelImpl implements VariantModel {

  /**
   * Constructs a new internal ARF variant.
   */
  public ARFVariant() {
    super();
    setId(IDGenerator.generate());
  }

  /**
   * Constructs a new internal ARF variant from the given ARF variant.
   * 
   * @param variant
   *          The ARF variant.
   */
  public ARFVariant(ARFVariant variant) {
    copy(variant);
    super.setId(IDGenerator.generate());
  }

  /**
   * Constructs a new internal ARF variant from the given ARF variant.
   * 
   * @param variant
   *          The variant.
   */
  public void fromObject(ARFVariant variant) {
    super.getAttributes().clear();
    super.getSelections().clear();
    super.getValues().clear();
    copy(variant);
  }

  @Override
  public boolean equals(Object obj) {
    boolean ret = super.equals(obj);
    if (super.getId() != null) {
      if (obj instanceof ARFVariant) {
        ret = super.getId().equals(((ARFVariant) obj).getId());
      }
    }
    return ret;
  }

  @Override
  public int hashCode() {
    int ret = super.hashCode();
    if (super.getId() != null) {
      if (super.getId() != null) {
        ret = super.getId().hashCode();
      }
    }
    return ret;
  }

  private void copy(ARFVariant variant) {
    super.setFeatureModel(variant.getFeatureModel());
    super.setId(variant.getId());
    super.setVersion(variant.getVersion());
    for (Attribute attribute : variant.getAttributes()) {
      Attribute a = copy(attribute);
      super.getAttributes().add(a);
    }
    for (VariantSelection selection : variant.getSelections()) {
      VariantSelection s = copy(selection);
      super.getSelections().add(s);
    }
    for (VariantValue value : variant.getValues()) {
      VariantValue v = copy(value);
      super.getValues().add(v);
    }
  }

  private Description copy(Description description) {
    Description d = null;
    if (description != null) {
      d = FeatureModelFactory.eINSTANCE.createDescription();
      d.setId(description.getId());
      d.setText(description.getText());
    }
    return d;
  }

  private Attribute copy(Attribute attribute) {
    Attribute a = FeatureModelFactory.eINSTANCE.createAttribute();
    a.setId(attribute.getId());
    a.setName(attribute.getName());
    a.setSetable(attribute.isSetable());
    AttributeValue v = copy(attribute.getDefaultValue());
    a.setDefaultValue(v);
    AttributeType t = copy(attribute.getType());
    a.setType(t);
    Description d = copy(attribute.getDescription());
    attribute.setDescription(d);
    return a;
  }

  private AttributeValue copy(AttributeValue value) {
    AttributeValue v = null;
    if (value instanceof AttributeValueString) {
      v = FeatureModelFactory.eINSTANCE.createAttributeValueString();
      ((AttributeValueString) v).setValue(((AttributeValueString) value).getValue());
    }
    else if (value instanceof AttributeValueInt) {
      v = FeatureModelFactory.eINSTANCE.createAttributeValueInt();
      ((AttributeValueInt) v).setValue(((AttributeValueInt) value).getValue());
    }
    else if (value instanceof AttributeValueBoolean) {
      v = FeatureModelFactory.eINSTANCE.createAttributeValueBoolean();
      ((AttributeValueBoolean) v).setValue(((AttributeValueBoolean) value).isValue());
    }
    else if (value instanceof AttributeValueEObject) {
      v = FeatureModelFactory.eINSTANCE.createAttributeValueEObject();
      ((AttributeValueEObject) v).setValue(((AttributeValueEObject) value).getValue());
    }
    return v;
  }

  private AttributeType copy(AttributeType type) {
    AttributeType t = null;
    if (type instanceof AttributeTypeString) {
      t = FeatureModelFactory.eINSTANCE.createAttributeTypeString();
    }
    else if (type instanceof AttributeTypeInt) {
      t = FeatureModelFactory.eINSTANCE.createAttributeTypeInt();
    }
    else if (type instanceof AttributeTypeBoolean) {
      t = FeatureModelFactory.eINSTANCE.createAttributeTypeBoolean();
    }
    else if (type instanceof AttributeTypeEObject) {
      t = FeatureModelFactory.eINSTANCE.createAttributeTypeEObject();
    }
    return t;
  }

  public static VariantSelection copy(VariantSelection selection) {
    VariantSelection s = VariantModelFactory.eINSTANCE.createVariantSelection();
    s.setId(selection.getId());
    s.setBound(selection.isBound());
    s.setState(selection.getState());
    s.setFeature(selection.getFeature());
    return s;
  }

  private VariantValue copy(VariantValue value) {
    VariantValue v = VariantModelFactory.eINSTANCE.createVariantValue();
    v.setId(value.getId());
    AttributeValue av = copy(value.getValue());
    v.setValue(av);
    v.setAttribute(value.getAttribute());
    return v;
  }
}
