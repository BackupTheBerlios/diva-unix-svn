package de.pure.diva.arf.pm.reasoner.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.util.Corrector;

public class NextPMRConfigurationListener extends PMRConfigurationListener {
  private Corrector corrector     = new Corrector();
  private Resource  model         = null;
  private Resource  configuration = null;
  public Resource   next          = null;

  public NextPMRConfigurationListener(Resource mResource, Resource cResource, ModelPartitioner partitioner, Mapper mapper, ARFModel model, ARFContext context) {
    super(partitioner, mapper, model, context);
    this.model = mResource;
    this.configuration = cResource;
  }

  @Override
  protected void handleResource(Resource resource) {
    if (this.next == null) {
      List<Resource> configurations = new ArrayList<Resource>();
      configurations.add(this.configuration);
      configurations.add(resource);
      this.next = this.corrector.correctNextConfiguration(this.model, this.configuration, configurations);
    }
    if (this.next != null) {
      super.finish();
    }
  }

}
