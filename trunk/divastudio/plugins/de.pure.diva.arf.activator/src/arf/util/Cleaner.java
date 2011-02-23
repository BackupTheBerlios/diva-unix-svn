package arf.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import diva.Context;
import diva.Dimension;
import diva.PriorityRule;
import diva.PropertyPriority;
import diva.PropertyValue;
import diva.Rule;
import diva.Scenario;
import diva.SimulationModel;
import diva.VariabilityModel;
import diva.VariableValue;
import diva.Variant;

public class Cleaner {
	  /**
	   * Clean the DiVA model.
	   * 
	   * @param model
	   *          The DiVA model.
	   * @throws Exception
	   */
	  public void clean(Resource model) throws Exception {
	    if (model != null) {
	      // find the VariabilityModel
	      for (EObject o : model.getContents()) {
	        if (o instanceof VariabilityModel) {
	          VariabilityModel m = (VariabilityModel) o;
	          // clean model
	          cleanModel(m);
	        }
	        else if (o instanceof diva_configuration.SuitableConfigurations) {
	          diva_configuration.SuitableConfigurations configurations = (diva_configuration.SuitableConfigurations) o;
	          cleanConfiguration(configurations);
	        }
	      }
	      model.setModified(true);
	      model.save(null);
	    }
	  }

	  private void cleanConfiguration(diva_configuration.SuitableConfigurations configurations) {
	    configurations.getConfigurations().clear();
	  }

	  /**
	   * Clean the DiVA model.
	   * 
	   * @param model
	   *          The DiVA model.
	   */
	  private void cleanModel(VariabilityModel model) {
	    SimulationModel simulation = model.getSimulation();
	    cleanSimulation(simulation);
	    EList<Dimension> dimensions = model.getDimension();
	    cleanDimensions(dimensions);
	    EList<Rule> rules = model.getRule();
	    cleanRules(rules);
	  }

	  /**
	   * Clean simulation model and remove simulation data.
	   * 
	   * @param simulation
	   *          The simulation model.
	   */
	  private void cleanSimulation(SimulationModel simulation) {
	    if (simulation != null) {
	      for (Scenario s : simulation.getScenario()) {
	        for (Context c : s.getContext()) {
	          c.getPriority().clear();
	          c.getConfiguration().clear();
	          List<VariableValue> toRemove = new ArrayList<VariableValue>();
	          for (VariableValue vv : c.getVariable()) {
	            if (vv.getVariable() == null) {
	              toRemove.add(vv);
	            }
	          }
	          for (VariableValue vv : toRemove) {
	            c.getVariable().remove(vv);
	          }
	        }
	      }
	    }
	  }

	  /**
	   * Clean dimensions and remove unused property values on variants.
	   * 
	   * @param dimensions
	   *          All dimensions.
	   */
	  private void cleanDimensions(EList<Dimension> dimensions) {
	    for (Dimension d : dimensions) {
	      for (Variant v : d.getVariant()) {
	        List<PropertyValue> toRemove = new ArrayList<PropertyValue>();
	        for (PropertyValue pv : v.getPropertyValue()) {
	          if (pv.getProperty() == null) {
	            toRemove.add(pv);
	          }
	        }
	        for (PropertyValue pv : toRemove) {
	          v.getPropertyValue().remove(pv);
	        }
	      }
	    }
	  }

	  /**
	   * Clean rules and remove unused PropertyPriority on rules.
	   * 
	   * @param rules
	   *          All rules.
	   */
	  private void cleanRules(EList<Rule> rules) {
	    for (Rule r : rules) {
	      if (r instanceof PriorityRule) {
	        PriorityRule pr = (PriorityRule) r;
	        List<PropertyPriority> toRemove = new ArrayList<PropertyPriority>();
	        for (PropertyPriority pp : pr.getPriority()) {
	          if (pp.getProperty() == null) {
	            toRemove.add(pp);
	          }
	        }
	        for (PropertyPriority pp : toRemove) {
	          pr.getPriority().remove(pp);
	        }
	      }
	    }
	  }
}
