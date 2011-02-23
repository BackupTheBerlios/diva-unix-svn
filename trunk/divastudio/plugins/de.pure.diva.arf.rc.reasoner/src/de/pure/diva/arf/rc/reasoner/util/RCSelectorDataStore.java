package de.pure.diva.arf.rc.reasoner.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arf.model.ARFConfiguration;

public class RCSelectorDataStore {

  private RCComparator                  comparator    = new RCComparator();
  private Set<String>                   unsatisfiable = new HashSet<String>();
  private Map<String, ARFConfiguration> satisfiable   = new HashMap<String, ARFConfiguration>();

  public String getHash(ARFConfiguration configuration) {
    return this.comparator.toString(configuration);
  }

  public boolean isUnsatisfiable(String hash) {
    boolean unsatisfiable = false;
    synchronized (this.unsatisfiable) {
      unsatisfiable = this.unsatisfiable.contains(hash);
    }
    return unsatisfiable;
  }

  public void addUnsatisfiable(String hash) {
    synchronized (this.unsatisfiable) {
      this.unsatisfiable.add(hash);
    }
  }

  public boolean isSatisfiable(String hash) {
    boolean satisfiable = false;
    synchronized (this.satisfiable) {
      satisfiable = this.satisfiable.containsKey(hash);
    }
    return satisfiable;
  }

  public boolean addSatisfiable(String hash, ARFConfiguration configuration) {
    boolean added = false;
    synchronized (this.satisfiable) {
      added = this.satisfiable.containsKey(hash) == false;
      this.satisfiable.put(hash, configuration);
    }
    return added;
  }

  public int getSatisfiableCount() {
    int count = 0;
    synchronized (this.satisfiable) {
      count = this.satisfiable.size();
    }
    return count;
  }

  public ARFConfiguration getSatisfiable(int position) {
    ARFConfiguration configuration = null;
    List<String> keys = new ArrayList<String>();
    synchronized (this.satisfiable) {
      keys.addAll(this.satisfiable.keySet());
    }
    String hash = keys.get(position);
    synchronized (this.satisfiable) {
      configuration = this.satisfiable.get(hash);
    }
    return configuration;
  }

  public boolean isPartOfSatisfiable(ARFConfiguration configuration) {
    boolean ispart = false;
    Set<ARFConfiguration> satisfiable = new HashSet<ARFConfiguration>();
    synchronized (this.satisfiable) {
      satisfiable.addAll(this.satisfiable.values());
    }
    ispart = this.comparator.part(satisfiable, configuration);
    return ispart;
  }
}
