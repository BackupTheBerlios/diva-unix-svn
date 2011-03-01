package service.arf.model;

import org.eclipse.emf.ecore.resource.Resource;

public interface Loader {

  public Resource load(String uri);
}
