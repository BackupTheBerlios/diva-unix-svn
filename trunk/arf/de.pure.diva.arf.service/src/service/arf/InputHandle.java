package service.arf;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * The input handle interface encapsulates the context model and configuration
 * model.
 */
public interface InputHandle {
  /**
   * Set the context by URI.
   * 
   * @param uri
   *          The context URI.
   */
  public void setContext(String uri);

  /**
   * Set the context by object.
   * 
   * @param resource
   *          The context resource object.
   */
  public void setContext(Resource resource);

  /**
   * Get the context resource object. If the context was set by URI it has to be
   * loaded.
   * 
   * @return The context resource object.
   */
  public Resource getContext();

  /**
   * Get the context URI string. If the context was set by a resource object no
   * URI is available.
   * 
   * @return The context URI string or <b>null</b>.
   */
  public String getContextURI();

  /**
   * Set the configuration by URI.
   * 
   * @param uri
   *          The configuration URI.
   */
  public void setConfiguration(String uri);

  /**
   * Set the configuration by object.
   * 
   * @param resource
   *          The configuration resource object.
   */
  public void setConfiguration(Resource resource);

  /**
   * Get the configuration resource object. If the configuration was set by URI
   * it has to be loaded.
   * 
   * @return The configuration resource object.
   */
  public Resource getConfiguration();

  /**
   * Get the configuration URI string. If the configuration was set by a
   * resource object no URI is available.
   * 
   * @return The configuration URI string or <b>null</b>.
   */
  public String getConfigurationURI();

}
