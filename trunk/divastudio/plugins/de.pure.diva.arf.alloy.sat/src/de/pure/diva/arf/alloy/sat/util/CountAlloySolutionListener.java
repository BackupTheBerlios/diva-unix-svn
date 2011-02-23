package de.pure.diva.arf.alloy.sat.util;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFContext;
import arf.model.ARFModel;

public class CountAlloySolutionListener extends AlloySolutionListener {

  public CountAlloySolutionListener(AlloyConnector connector, Mapper mapper, ARFModel model, ARFContext context) {
    super(connector, mapper, model, context);
  }

  @Override
  public void addAtoms(List<String> atoms) {
  // nothing to do
  }

  @Override
  protected void handleResource(Resource resource) {}

}
