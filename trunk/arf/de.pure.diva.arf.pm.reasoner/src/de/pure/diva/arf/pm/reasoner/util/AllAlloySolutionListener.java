package de.pure.diva.arf.pm.reasoner.util;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;
import de.pure.diva.arf.alloy.sat.util.AlloyConnector;

public class AllAlloySolutionListener extends PMRAlloySolutionListener {

  private ConfigurationResultMerger merger = null;

  public AllAlloySolutionListener(ConfigurationResultMerger merger, String colour, AlloyConnector connector, Mapper mapper, ARFModel model, ARFContext context) {
    super(colour, connector, mapper, model, context);
    this.merger = merger;
  }

  @Override
  protected void handleConfiguration(ARFConfiguration configuration) {
    this.merger.addConfiguration(colour(), configuration);
  }

  @Override
  protected void handleResource(Resource resource) {}

  @Override
  public void setSuccess() {
    super.setSuccess();
    this.merger.setSuccess(colour());
  }

  @Override
  public void setFault(String fault) {
    super.setFault(fault);
    this.merger.setFault(colour(), getFaultMessage());
  }

  @Override
  protected synchronized void finish() {
    super.finish();
    if (hasResult() == false) {
      this.merger.finish(colour());
    }
  }

}
