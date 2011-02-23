package service.arf.model;

import org.eclipse.emf.ecore.resource.Resource;

import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;

public interface Mapper {

  public ARFConfiguration mapConfiguration(Resource model, Resource context, Resource configuration);

  public Resource mapConfiguration(ARFModel model, ARFContext context, ARFConfiguration configuration);

  public ARFContext mapContext(Resource model, Resource context);

  public Resource mapContext(ARFModel model, ARFContext context);

  public ARFModel mapModel(Resource model);

  public Resource mapModel(ARFModel model);

}
