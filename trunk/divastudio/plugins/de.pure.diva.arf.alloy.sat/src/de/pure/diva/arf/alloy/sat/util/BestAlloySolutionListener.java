package de.pure.diva.arf.alloy.sat.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.util.Corrector;

public class BestAlloySolutionListener extends AlloySolutionListener {

  private Corrector corrector = new Corrector();
  private Resource  model     = null;
  public Resource   best      = null;

  public BestAlloySolutionListener(Resource mResource, AlloyConnector connector, Mapper mapper, ARFModel model, ARFContext context) {
    super(connector, mapper, model, context);
    this.model = mResource;
  }

  @Override
  protected void handleResource(Resource resource) {
    if (this.best == null) {
      this.best = resource;
    }
    else {
      List<Resource> configurations = new ArrayList<Resource>();
      configurations.add(this.best);
      configurations.add(resource);
      this.best = this.corrector.correctBestConfiguration(this.model, configurations);
    }
  }
}
