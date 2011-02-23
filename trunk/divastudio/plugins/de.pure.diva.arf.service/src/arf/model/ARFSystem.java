package arf.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
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
import org.eclipse.featuremodel.Constraint;
import org.eclipse.featuremodel.Description;
import org.eclipse.featuremodel.Feature;
import org.eclipse.featuremodel.FeatureModel;
import org.eclipse.featuremodel.FeatureModelFactory;
import org.eclipse.featuremodel.Group;
import org.eclipse.featuremodel.impl.FeatureModelImpl;

/**
 * The ARFSystem represents the internal reasoning model of the framework. It is
 * implemented as an EMF Feature Model.
 * 
 * @see FeatureModel
 */
public class ARFSystem extends FeatureModelImpl implements FeatureModel {

  /**
   * Constructs a new internal ARF model.
   */
  public ARFSystem() {
    super();
    super.setId(IDGenerator.generate());
  }

  public ARFSystem(ARFSystem system) {
    copy(system);
    super.setId(IDGenerator.generate());
  }

  /**
   * Loads a FeatureModel from URI.
   * 
   * @param uri
   *          The URI.
   * @return The FeatureModel.
   */
  public static FeatureModel loadModel(String uri) {
    FeatureModel model = load(load(uri));
    if (model != null && model.getId() == null) {
      model.setId(IDGenerator.generate());
    }
    return model;
  }

  private static FeatureModel load(Resource resource) {
    FeatureModel model = null;
    if (resource != null) {
      Object object = resource.getContents().isEmpty() == false ? resource.getContents().get(0) : null;
      if (object instanceof FeatureModel) {
        model = (FeatureModel) object;
      }
    }
    if (model != null && model.getId() == null) {
      model.setId(IDGenerator.generate());
    }
    return model;
  }

  private static Resource load(String uri) {
    Resource resource = null;
    if (uri != null) {
      ResourceSet rs = new ResourceSetImpl();
      URI ecoreFileURI = URI.createPlatformResourceURI(uri, true);
      resource = rs.getResource(ecoreFileURI, true);
    }
    return resource;
  }

  @Override
  public boolean equals(Object obj) {
    boolean ret = super.equals(obj);
    if (super.getId() != null) {
      if (obj instanceof ARFSystem) {
        ret = super.getId().equals(((ARFSystem) obj).getId());
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

  protected void copy(ARFSystem system) {
    super.setId(system.getId());
    Description d = copy(system.getDescription());
    super.setDescription(d);
    super.setVersion(system.getVersion());
    for (Attribute attribute : system.getAttributes()) {
      Attribute a = copy(attribute);
      super.getAttributes().add(a);
    }
    for (Constraint constraint : system.getConstraints()) {
      Constraint c = copy(constraint);
      super.getConstraints().add(c);
    }
    Feature f = copy(system.getRoot());
    super.setRoot(f);
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

  private Constraint copy(Constraint constraint) {
    Constraint c = FeatureModelFactory.eINSTANCE.createConstraint();
    c.setId(constraint.getId());
    c.setLanguage(constraint.getLanguage());
    c.setCode(constraint.getCode());
    Description d = copy(constraint.getDescription());
    c.setDescription(d);
    return c;
  }

  private Feature copy(Feature feature) {
    Feature f = FeatureModelFactory.eINSTANCE.createFeature();
    f.setId(feature.getId());
    f.setName(feature.getName());
    f.setType(feature.getType());
    Description d = copy(feature.getDescription());
    f.setDescription(d);
    for (Attribute attribute : feature.getAttributes()) {
      Attribute a = copy(attribute);
      f.getAttributes().add(a);
    }
    for (Group group : feature.getChildren()) {
      Group g = copy(group);
      f.getChildren().add(g);
    }
    return f;
  }

  private Group copy(Group group) {
    Group g = FeatureModelFactory.eINSTANCE.createGroup();
    g.setId(group.getId());
    g.setLower(group.getLower());
    g.setUpper(group.getUpper());
    for (Feature feature : group.getFeatures()) {
      Feature f = copy(feature);
      g.getFeatures().add(f);
    }
    return g;
  }
}
