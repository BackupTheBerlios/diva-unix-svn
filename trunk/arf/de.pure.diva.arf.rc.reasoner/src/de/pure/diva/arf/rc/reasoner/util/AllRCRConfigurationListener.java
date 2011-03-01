package de.pure.diva.arf.rc.reasoner.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFContext;
import arf.model.ARFModel;

public class AllRCRConfigurationListener extends RCRConfigurationListener {

  public List<Resource> all = new ArrayList<Resource>();

  public AllRCRConfigurationListener(RCSelector selector, Mapper mapper, ARFModel model, ARFContext context) {
    super(new RCComparator(), selector, mapper, model, context);
  }

  @Override
  protected void handleResource(Resource resource) {
    synchronized (this.all) {
      this.all.add(resource);
    }
  }

}
