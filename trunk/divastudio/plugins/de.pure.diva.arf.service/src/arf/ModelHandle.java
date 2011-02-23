package arf;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Loader;

public class ModelHandle implements service.arf.ModelHandle {

  private Loader   loader   = null;
  private Resource model    = null;
  private String   modelURI = null;

  public ModelHandle(Loader loader) {
    setLoader(loader);
  }

  public ModelHandle(Resource resource) {
    setModel(resource);
  }

  public ModelHandle(Loader loader, String uri) {
    setLoader(loader);
    setModel(uri);
  }

  public Resource getModel() {
    if (this.model == null && this.loader != null) {
      this.model = this.loader.load(modelURI);
    }
    return this.model;
  }

  public String getModelURI() {
    String uri = modelURI;
    if (uri == null && model != null && model.getURI() != null) {
      uri = model.getURI().toString();
    }
    return uri;
  }

  public Loader getLoader() {
    return this.loader;
  }

  public void setModel(String uri) {
    this.modelURI = uri;
  }

  public void setModel(Resource resource) {
    this.model = resource;
  }

  public void setLoader(Loader loader) {
    this.loader = loader;
  }
}
