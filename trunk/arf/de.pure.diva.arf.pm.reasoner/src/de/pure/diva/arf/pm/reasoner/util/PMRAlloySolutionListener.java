package de.pure.diva.arf.pm.reasoner.util;

import service.arf.model.Mapper;
import arf.model.ARFContext;
import arf.model.ARFModel;
import de.pure.diva.arf.alloy.sat.util.AlloyConnector;
import de.pure.diva.arf.alloy.sat.util.AlloySolutionListener;

public abstract class PMRAlloySolutionListener extends AlloySolutionListener {

  private String colour = null;

  public PMRAlloySolutionListener(String colour, AlloyConnector connector, Mapper mapper, ARFModel model, ARFContext context) {
    super(connector, mapper, model, context);
    this.colour = colour;
  }

  @Override
  protected synchronized void finish() {
    super.finish();
  }

  public String colour() {
    return this.colour;
  }
}
