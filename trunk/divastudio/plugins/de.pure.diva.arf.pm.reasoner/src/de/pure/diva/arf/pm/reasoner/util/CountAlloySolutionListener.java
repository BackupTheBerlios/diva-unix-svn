package de.pure.diva.arf.pm.reasoner.util;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

import service.arf.model.Mapper;
import arf.model.ARFContext;
import arf.model.ARFModel;
import de.pure.diva.arf.alloy.sat.util.AlloyConnector;

public class CountAlloySolutionListener extends PMRAlloySolutionListener {

  private CountResultMerger merger = null;

  public CountAlloySolutionListener(CountResultMerger merger, String colour, AlloyConnector connector, Mapper mapper, ARFModel model, ARFContext context) {
    super(colour, connector, mapper, model, context);
    this.merger = merger;
  }

  @Override
  public void addAtoms(List<String> atoms) {
  // nothing to do
  }

  @Override
  protected void handleResource(Resource resource) {}

  @Override
  public void setSuccess() {
    super.setSuccess();
    this.merger.setSuccess(colour(), count());
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
