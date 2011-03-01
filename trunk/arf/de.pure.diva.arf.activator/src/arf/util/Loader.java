package arf.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class Loader extends arf.model.util.Loader implements service.arf.model.Loader {
  public Resource load(String uri) {
    Resource resource = null;
    if (uri != null) {
      ResourceSet rs = new ResourceSetImpl();
      URI ecoreFileURI = URI.createFileURI(uri);
      resource = rs.getResource(ecoreFileURI, true);
    }
    return resource;
  }

}
