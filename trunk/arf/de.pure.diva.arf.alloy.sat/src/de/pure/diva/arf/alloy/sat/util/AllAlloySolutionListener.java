package de.pure.diva.arf.alloy.sat.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFContext;
import arf.model.ARFModel;

public class AllAlloySolutionListener extends AlloySolutionListener {

  public List<Resource> all = new ArrayList<Resource>();

  public AllAlloySolutionListener(AlloyConnector connector, Mapper mapper, ARFModel model, ARFContext context) {
    super(connector, mapper, model, context);
  }

  @Override
  protected void handleResource(Resource resource) {
    this.all.add(resource);
  }

}
