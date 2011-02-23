package de.pure.diva.arf.alloy.sat.util;

import java.util.ArrayList;
import java.util.List;

import arf.SolverListener;
import arf.model.ARFConfiguration;
import arf.model.ARFModel;

public class AlloyConnectorListener extends SolverListener {
  private static final String                     ALLOY_ATOM_INVALID = "\\$0";
  private AlloyConnector                          connector          = null;
  private ARFModel                                model              = null;
  private ARFConfiguration                        configuration      = null;
  private AlloySolution2ARFConfigurationConverter cGenerator         = new AlloySolution2ARFConfigurationConverter();

  public AlloyConnectorListener(AlloyConnector connector, ARFModel model) {
    this.connector = connector;
    this.model = model;
  }

  public AlloyConnectorListener(AlloyConnector connector, ARFModel model, ARFConfiguration configuration) {
    this(connector, model);
    this.configuration = configuration;
  }

  public void synchronise(long timeout) throws Exception {
    super.synchronise("AlloyConnector::Timeouter", timeout);
  }

  synchronized protected void finish() {
    if (hasResult() == false) {
      this.connector.cancel(this);
    }
    super.finish();
  }

  public void addAtoms(List<String> atoms) {
    handleAtoms(atoms);
  }

  protected void handleAtoms(List<String> atoms) {
    List<String> tmp_atoms = new ArrayList<String>();
    for (String atom : atoms) {
      atom = atom.replaceAll(ALLOY_ATOM_INVALID, "");
      tmp_atoms.add(atom);
    }
    ARFConfiguration configuration = null;
    if (this.configuration != null) {
      configuration = cGenerator.convert(tmp_atoms, new ARFConfiguration(this.configuration), this.model);
    }
    else {
      configuration = cGenerator.convert(tmp_atoms, this.model);
    }
    handleConfiguration(configuration);
  }

  protected void handleConfiguration(ARFConfiguration configuration) {}

  public int count() {
    int count = this.connector.count();
    if (hasSuccess() == false) {
      count = 0;
    }
    return count;
  }
}
