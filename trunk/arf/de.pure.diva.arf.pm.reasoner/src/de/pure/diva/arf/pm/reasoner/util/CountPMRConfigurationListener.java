package de.pure.diva.arf.pm.reasoner.util;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;

public class CountPMRConfigurationListener extends PMRConfigurationListener {

  public CountPMRConfigurationListener(ModelPartitioner partitioner, Mapper mapper, ARFModel model, ARFContext context) {
    super(partitioner, mapper, model, context);
  }

  @Override
  protected void handleConfiguration(ARFConfiguration configuration) {}

  @Override
  protected void handleResource(Resource resource) {}

}
