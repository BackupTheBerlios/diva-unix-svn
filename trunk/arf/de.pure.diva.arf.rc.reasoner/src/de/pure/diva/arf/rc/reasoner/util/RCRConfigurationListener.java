package de.pure.diva.arf.rc.reasoner.util;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;

public abstract class RCRConfigurationListener extends RCSelectorParallelListener {

  private Mapper     mapper  = null;
  private ARFModel   model   = null;
  private ARFContext context = null;

  public RCRConfigurationListener(RCComparator comparator, RCSelector selector, Mapper mapper, ARFModel model, ARFContext context) {
    super(comparator, selector);
    this.mapper = mapper;
    this.model = model;
    this.context = context;
  }

  protected void handleConfiguration(ARFConfiguration configuration) {
    Resource resource = mapper.mapConfiguration(this.model, this.context, configuration);
    handleResource(resource);
  }

  protected abstract void handleResource(Resource resource);

}
