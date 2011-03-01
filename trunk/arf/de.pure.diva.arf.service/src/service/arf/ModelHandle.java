package service.arf;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * The model handle interface encapsulates the reasoning model. It can handle
 * the model by URI or by object reference.
 */
public interface ModelHandle {

  /**
   * Set the model by URI.
   * 
   * @param uri
   *          The model URI.
   */
  public void setModel(String uri);

  /**
   * Set the model by object.
   * 
   * @param resource
   *          The model resource object.
   */
  public void setModel(Resource resource);

  /**
   * Get the model resource object. If the model was set by URI it has to be
   * loaded.
   * 
   * @return The model resource object.
   */
  public Resource getModel();

  /**
   * Get the model URI string. If the model was set by a resource object no URI
   * is available.
   * 
   * @return The model URI string or <b>null</b>.
   */
  public String getModelURI();

}
