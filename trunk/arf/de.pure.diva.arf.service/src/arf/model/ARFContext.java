package arf.model;

import org.eclipse.variantmodel.VariantModel;

/**
 * The ARFVariant represents the internal context model of the framework. It is
 * implemented as an EMF Variant Model.
 * 
 * @see VariantModel
 */
public class ARFContext extends ARFVariant implements VariantModel {

  /**
   * Constructs a new internal ARF context.
   */
  public ARFContext() {
    super();
  }

  /**
   * Constructs a new internal ARF context from the given ARF context.
   * 
   * @param context
   *          The ARF context.
   */
  public ARFContext(ARFContext context) {
    super(context);
  }

  /**
   * Constructs a new internal ARF context from the given ARF context.
   * 
   * @param context
   *          The context.
   */
  public void fromObject(ARFContext context) {
    super.fromObject(context);
  }

  @Override
  public boolean equals(Object obj) {
    boolean ret = super.equals(obj);
    if (super.getId() != null) {
      if (obj instanceof ARFContext) {
        ret = super.getId().equals(((ARFContext) obj).getId());
      }
    }
    return ret;
  }

}
