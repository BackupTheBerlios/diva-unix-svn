package arf.model.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import diva.DivaPackage;
import diva_configuration.Diva_configurationPackage;
import diva_context.Diva_contextPackage;
import fr.irisa.triskell.eclipse.emf.EMFRegistryHelper;

public class Loader implements service.arf.model.Loader {

  public Resource load(String uri) {
    Resource resource = null;
    if (uri != null) {
      resource = registerDiVAModel(uri);
      resource = registerDiVAContext(uri);
    }
    return resource;
  }

  private Resource registerDiVAModel(String uri) {
    Resource resource;
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("diva", new XMIResourceFactoryImpl());
    EMFRegistryHelper.safeRegisterPackages(EPackage.Registry.INSTANCE, DivaPackage.eINSTANCE);
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("diva", new XMIResourceFactoryImpl() {
      public Resource createResource(URI uri) {
        XMIResource xmiResource = new XMIResourceImpl(uri);
        return xmiResource;
      }
    });
    ResourceSet rs = new ResourceSetImpl();
    URI ecoreFileURI = URI.createURI(uri);
    resource = rs.getResource(ecoreFileURI, true);
    return resource;
  }

  private Resource registerDiVAContext(String uri) {
    Resource resource;
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    EMFRegistryHelper.safeRegisterPackages(EPackage.Registry.INSTANCE, Diva_contextPackage.eINSTANCE);
    EMFRegistryHelper.safeRegisterPackages(EPackage.Registry.INSTANCE, Diva_configurationPackage.eINSTANCE);
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl() {
      public Resource createResource(URI uri) {
        XMIResource xmiResource = new XMIResourceImpl(uri);
        return xmiResource;
      }
    });
    ResourceSet rs = new ResourceSetImpl();
    URI ecoreFileURI = URI.createURI(uri);
    resource = rs.getResource(ecoreFileURI, true);
    return resource;
  }
}
