package de.pure.diva.arf.pm.reasoner.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExistResultMerger {

  private ModelPartitioner.Partitioner partitioner = null;
  private ModelPartitionerListener     listener    = null;
  private Map<String, Boolean>         results     = Collections.synchronizedMap(new HashMap<String, Boolean>()); ;

  public ExistResultMerger(ModelPartitioner.Partitioner partitioner, ModelPartitionerListener listener, Set<String> colours) {
    this.partitioner = partitioner;
    this.listener = listener;
    for (String colour : colours) {
      this.results.put(colour, null);
    }
  }

  public void setSuccess(String colour) {
    setResult(colour, null);
  }

  public void setFault(String colour, String fault) {
    setResult(colour, fault);
  }

  public void finish(String colour) {
    setResult(colour, "");
  }

  private void setResult(String colour, String fault) {
    if (fault == null) {
      this.results.put(colour, Boolean.TRUE);
      boolean result = true;
      synchronized (this.results) {
        for (String r : this.results.keySet()) {
          Boolean b = this.results.get(r);
          if (b != null) {
            result = result || b.booleanValue();
          }
          else {
            result = false;
            break;
          }
        }
      }
      if (result != false) {
        this.listener.setSuccess();
      }
    }
    else if ("".equals(fault) == true) {
      this.listener.finish();
      this.partitioner.interrupt();
    }
    else {
      this.listener.setFault(fault);
      this.partitioner.interrupt();
    }
  }

}
