package de.pure.diva.arf.rc.reasoner.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import arf.model.ARFConfiguration;

public class RCSelectorParallelListener extends RCSelectorListener {

  private RCComparator comparator     = null;
  private Set<String>  configurations = Collections.synchronizedSet(new HashSet<String>());

  public RCSelectorParallelListener(RCComparator comparator, RCSelector selector) {
    super(selector);
    this.comparator = comparator;
  }

  @Override
  public void addConfiguration(ARFConfiguration configuration) {
    String hash = null;
    if (this.comparator != null) {
      hash = this.comparator.toString(configuration);
    }
    if (hash == null) {
      super.addConfiguration(configuration);
    }
    else {
      boolean add = false;
      synchronized (this) {
        if (this.configurations.contains(hash) == false) {
          this.configurations.add(hash);
          add = true;
        }
      }
      if (add == true) {
        super.addConfiguration(configuration);
      }
    }
  }

  @Override
  public int count() {
    int count = this.configurations.size();
    if (hasResult() == true && hasSuccess() == false) {
      count = 0;
    }
    return count;
  }
}
