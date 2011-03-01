package de.pure.diva.arf.pm.reasoner.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFContext;
import arf.model.ARFModel;

public class AllPMRConfigurationListener extends PMRConfigurationListener {

  public List<Resource> all = new ArrayList<Resource>();

  public AllPMRConfigurationListener(ModelPartitioner partitioner, Mapper mapper, ARFModel model, ARFContext context) {
    super(partitioner, mapper, model, context);
  }

  @Override
  protected void handleResource(Resource resource) {
    this.all.add(resource);
  }

}
