package de.pure.diva.arf.pm.reasoner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arf.model.ARFConfiguration;
import arf.model.ARFModel;

public class ConfigurationResultMerger extends ExistResultMerger {

  private ModelPartitionerListener              listener       = null;
  private ARFModel                              model          = null;
  protected Map<String, List<ARFConfiguration>> configurations = Collections.synchronizedMap(new HashMap<String, List<ARFConfiguration>>()); ;

  public ConfigurationResultMerger(ModelPartitioner.Partitioner partitioner, ModelPartitionerListener listener, Set<String> colours, ARFModel model) {
    super(partitioner, listener, colours);
    this.listener = listener;
    this.model = model;
    for (String colour : colours) {
      this.configurations.put(colour, new ArrayList<ARFConfiguration>());
    }
  }

  public void setSuccess(String colour) {
    super.setSuccess(colour);
  }

  public void setFault(String colour, String fault) {
    super.setFault(colour, fault);
  }

  public void finish(String colour) {
    super.finish(colour);
  }

  public void addConfiguration(String colour, ARFConfiguration configuration) {
    this.configurations.get(colour).add(configuration);
    Map<String, List<ARFConfiguration>> result = null;
    synchronized (this.configurations) {
      boolean r = false;
      for (String c : this.configurations.keySet()) {
        r = this.configurations.get(c).isEmpty() == false;
        if (r == false) {
          break;
        }
      }
      if (r == true) {
        result = new HashMap<String, List<ARFConfiguration>>(this.configurations);
        result.remove(colour);
      }
    }
    if (result != null) {
      List<List<ARFConfiguration>> toBeMerged = collect(configuration, result.values());
      List<ARFConfiguration> merged = merge(this.model, toBeMerged);
      for (ARFConfiguration mergedConfiguration : merged) {
        this.listener.addConfiguration(mergedConfiguration);
      }
    }
  }

  private List<List<ARFConfiguration>> collect(ARFConfiguration configuration, Collection<List<ARFConfiguration>> colouredConfigurationsLists) {
    List<List<ARFConfiguration>> configurationsListsToBeMerged = new ArrayList<List<ARFConfiguration>>();
    {
      List<ARFConfiguration> configurationsToBeMerged = new ArrayList<ARFConfiguration>();
      configurationsToBeMerged.add(configuration);
      configurationsListsToBeMerged.add(configurationsToBeMerged);
    }
    for (List<ARFConfiguration> colouredConfigurations : colouredConfigurationsLists) {
      List<List<ARFConfiguration>> newConfigurationsListsToBeMerged = new ArrayList<List<ARFConfiguration>>();
      for (List<ARFConfiguration> configurationsToBeMerged : configurationsListsToBeMerged) {
        for (ARFConfiguration colouredConfiguration : colouredConfigurations) {
          List<ARFConfiguration> newConfigurationsToBeMerged = new ArrayList<ARFConfiguration>(configurationsToBeMerged);
          newConfigurationsToBeMerged.add(colouredConfiguration);
          newConfigurationsListsToBeMerged.add(newConfigurationsToBeMerged);
        }
      }
      configurationsListsToBeMerged = newConfigurationsListsToBeMerged;
    }
    return configurationsListsToBeMerged;
  }

  private List<ARFConfiguration> merge(ARFModel model, List<List<ARFConfiguration>> toBeMerged) {
    List<ARFConfiguration> merged = new ArrayList<ARFConfiguration>();
    ARFConfigurationMerger merger = new ARFConfigurationMerger();
    for (List<ARFConfiguration> configurations : toBeMerged) {
      ARFConfiguration configuration = merger.convert(configurations, model);
      merged.add(configuration);
    }
    return merged;
  }
}
