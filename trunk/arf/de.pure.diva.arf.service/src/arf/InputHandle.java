package arf;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Loader;

public class InputHandle implements service.arf.InputHandle {

  private Loader   loader           = null;
  private Resource context          = null;
  private Resource configuration    = null;
  private String   contextURI       = null;
  private String   configurationURI = null;

  public InputHandle(Loader loader) {
    setLoader(loader);
  }

  public InputHandle(Loader loader, String contextURI, String configurationURI) {
    setLoader(loader);
    setContext(contextURI);
    setConfiguration(configurationURI);
  }

  public InputHandle(Resource context, Resource configuration) {
    setContext(context);
    setConfiguration(configuration);
  }

  public Resource getConfiguration() {
    if (this.configuration == null && this.loader != null) {
      this.configuration = this.loader.load(configurationURI);
    }
    return this.configuration;
  }

  public String getConfigurationURI() {
    String uri = configurationURI;
    if (uri == null && configuration != null && configuration.getURI() != null) {
      uri = configuration.getURI().toString();
    }
    return uri;
  }

  public Resource getContext() {
    if (this.context == null && this.loader != null) {
      this.context = this.loader.load(contextURI);
    }
    return this.context;
  }

  public String getContextURI() {
    String uri = contextURI;
    if (uri == null && context != null && context.getURI() != null) {
      uri = context.getURI().toString();
    }
    return uri;
  }

  public Loader getLoader() {
    return this.loader;
  }

  public void setConfiguration(String uri) {
    this.configurationURI = uri;
  }

  public void setConfiguration(Resource resource) {
    this.configuration = resource;
  }

  public void setContext(String uri) {
    this.contextURI = uri;
  }

  public void setContext(Resource resource) {
    this.context = resource;
  }

  public void setLoader(Loader loader) {
    this.loader = loader;
  }
}
