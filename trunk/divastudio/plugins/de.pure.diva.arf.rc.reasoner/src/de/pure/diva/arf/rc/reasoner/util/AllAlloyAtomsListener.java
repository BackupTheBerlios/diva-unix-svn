package de.pure.diva.arf.rc.reasoner.util;

import arf.model.ARFConfiguration;
import arf.model.ARFModel;
import de.pure.diva.arf.alloy.sat.util.AlloyConnector;
import de.pure.diva.arf.alloy.sat.util.AlloyConnectorListener;

public class AllAlloyAtomsListener extends AlloyConnectorListener {

  private RCSelector.Selector selector      = null;
  boolean                     satisfiable   = false;
  ARFConfiguration            configuration = null;

  public AllAlloyAtomsListener(RCSelector.Selector selector, ARFConfiguration configuration, AlloyConnector connector, ARFModel model) {
    super(connector, model, new ARFConfiguration(configuration));
    this.selector = selector;
    this.configuration = configuration;
  }

  public boolean satisfiable() {
    return satisfiable;
  }

  @Override
  protected void handleConfiguration(ARFConfiguration configuration) {
    this.satisfiable = true;
    this.selector.addConfiguration(configuration);
    if (selector.isReady() == true) {
      this.configuration.fromObject(configuration);
      this.finish();
    }
  }

}
