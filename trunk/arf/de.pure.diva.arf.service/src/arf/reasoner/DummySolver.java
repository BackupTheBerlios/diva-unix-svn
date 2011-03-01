package arf.reasoner;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import service.arf.InputHandle;
import service.arf.ModelHandle;
import service.arf.reasoner.Solver;
import arf.question.ConfigurationResult;

public class DummySolver implements Solver {

  private Resource createConfigurationResource(String parent) {
    // Create a configuration
    int totalScore = 0;
    diva_configuration.SuitableConfigurations configurationModel = diva_configuration.Diva_configurationFactory.eINSTANCE.createSuitableConfigurations();
    diva_configuration.Configuration suitableConfiguration = diva_configuration.Diva_configurationFactory.eINSTANCE.createConfiguration();
    suitableConfiguration.setScore(totalScore);
    configurationModel.getConfigurations().add(suitableConfiguration);
    // Create a URI
    URI uri = null;
    if (parent == null) {
      try {
        File temp = File.createTempFile("", "_result_0.xmi");
        // temp.deleteOnExit();
        String path = temp.toURI().toString();
        uri = URI.createURI(path);
      }
      catch (IOException e) {}
    }
    else {
      String path = parent.toString() + "_result_0.xmi";
      uri = URI.createURI(path);
    }
    // Create a resource
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.createResource(uri);
    if (resource != null) {
      resource.getContents().add(configurationModel);
    }
    return resource;
  }

  public ConfigurationResult solve(ModelHandle model, InputHandle input) {
    String uri = input.getContextURI();
    ConfigurationResult result = new ConfigurationResult();
    Resource configuration = createConfigurationResource(uri);
    result.getAnswer().add(configuration);
    return result;
  }

}
