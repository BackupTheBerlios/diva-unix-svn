package arf.model.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import diva.Configuration;
import diva.Context;
import diva.Scenario;
import diva.VariabilityModel;
import diva_configuration.SuitableConfigurations;

public class Corrector {

  public void correctNoConfiguration(Resource model, List<Resource> configurations) {
    Context context = getContext(model);
    if (context != null) {
      context.getConfiguration().clear();
    }
  }

  public Resource correctNextConfiguration(Resource model, Resource configuration, List<Resource> configurations) {
    Resource result = null;
    Set<String> aspects = new HashSet<String>();
    if (configuration != null) {
      aspects = getConfigurationAspects(configuration);
    }
    for (Iterator<Resource> idx = configurations.iterator(); idx.hasNext() == true && result == null;) {
      Resource resource = idx.next();
      Set<String> tmp = getConfigurationAspects(resource);
      if (tmp.size() != aspects.size() || tmp.containsAll(aspects) == false) {
        result = resource;
        break;
      }
    }
    return result;
  }

  public Resource correctBestConfiguration(Resource model, List<Resource> configurations) {
    int score = Integer.MIN_VALUE;
    Resource result = null;
    for (Resource resource : configurations) {
      int current = getConfigurationScore(resource);
      if (result == null || current > score) {
        result = resource;
        score = current;
      }
    }
    Context context = getContext(model);
    if (context != null) {
      if (result != null) {
        List<Configuration> toDelete = new ArrayList<Configuration>();
        for (Configuration configuration : context.getConfiguration()) {
          int tmp = configuration.getTotalScore();
          if (tmp != score) {
            toDelete.add(configuration);
          }
        }
        for (Configuration configuration : toDelete) {
          context.getConfiguration().remove(configuration);
        }
        while (context.getConfiguration().size() > 1) {
          context.getConfiguration().remove(1);
        }
      }
      else {
        context.getConfiguration().clear();
      }
    }
    return result;
  }

  private Context getContext(Resource resourceModel) {
    Context context = null;
    if (resourceModel != null) {
      EObject object = resourceModel.getContents().get(0);
      if (object instanceof VariabilityModel) {
        VariabilityModel model = (VariabilityModel) object;
        Scenario scenario = ARFModeling.getReasoningScenario(model);
        if (scenario != null) {
          context = scenario.getContext().get(0);
        }
      }
    }
    return context;
  }

  private int getConfigurationScore(Resource resource) {
    int score = Integer.MIN_VALUE;
    if (resource.getContents().isEmpty() == false) {
      EObject o = resource.getContents().get(0);
      if (o instanceof diva_configuration.SuitableConfigurations) {
        diva_configuration.SuitableConfigurations suitable = (SuitableConfigurations) o;
        diva_configuration.Configuration configuration = suitable.getConfigurations().get(0);
        if (configuration != null) {
          score = configuration.getScore();
        }
      }
    }
    return score;
  }

  private Set<String> getConfigurationAspects(Resource resource) {
    Set<String> aspects = new HashSet<String>();
    if (resource.getContents().isEmpty() == false) {
      EObject o = resource.getContents().get(0);
      if (o instanceof diva_configuration.SuitableConfigurations) {
        diva_configuration.SuitableConfigurations suitable = (SuitableConfigurations) o;
        diva_configuration.Configuration configuration = suitable.getConfigurations().get(0);
        if (configuration != null) {
          EList<diva_configuration.Aspect> list = configuration.getAspect();
          for (diva_configuration.Aspect aspect : list) {
            String name = aspect.getName();
            aspects.add(name);
          }
        }
      }
    }
    return aspects;
  }
}
