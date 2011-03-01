package arf.model;

import org.eclipse.variantmodel.VariantModel;

/**
 * The ARFVariant represents the internal configuration model of the framework.
 * It is implemented as an EMF Variant Model.
 * 
 * @see VariantModel
 */
public class ARFConfiguration extends ARFVariant implements VariantModel {

  /**
   * Constructs a new internal ARF configuration.
   */
  public ARFConfiguration() {
    super();
  }

  /**
   * Constructs a new internal ARF configuration from the given ARF
   * configuration.
   * 
   * @param configuration
   *          The ARF configuration.
   */
  public ARFConfiguration(ARFConfiguration configuration) {
    super(configuration);
  }

  /**
   * Constructs a new internal ARF configuration from the given ARF
   * configuration.
   * 
   * @param configuration
   *          The configuration.
   */
  public void fromObject(ARFConfiguration configuration) {
    super.fromObject(configuration);
  }

  @Override
  public boolean equals(Object obj) {
    boolean ret = super.equals(obj);
    if (super.getId() != null) {
      if (obj instanceof ARFConfiguration) {
        ret = super.getId().equals(((ARFConfiguration) obj).getId());
      }
    }
    return ret;
  }

}
