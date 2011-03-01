package de.pure.diva.arf.pm.reasoner.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountResultMerger {

  private ModelPartitioner.Partitioner partitioner = null;
  private ModelPartitionerListener     listener    = null;
  private Map<String, Integer>         counts      = Collections.synchronizedMap(new HashMap<String, Integer>());

  public CountResultMerger(ModelPartitioner.Partitioner partitioner, ModelPartitionerListener listener, Set<String> colours) {
    this.partitioner = partitioner;
    this.listener = listener;
    for (String colour : colours) {
      this.counts.put(colour, -1);
    }
  }

  public void setSuccess(String colour, int count) {
    setCount(colour, count, null);
  }

  public void setFault(String colour, String fault) {
    setCount(colour, 0, fault);
  }

  public void finish(String colour) {
    setCount(colour, -1, null);
  }

  private void setCount(String colour, int count, String fault) {
    if (count > 0) {
      this.counts.put(colour, count);
      int result = 1;
      synchronized (this.counts) {
        for (String c : this.counts.keySet()) {
          int i = this.counts.get(c);
          if (i != -1) {
            result = result * i;
          }
          else {
            result = -1;
            break;
          }
        }
      }
      if (result != -1) {
        this.partitioner.setCount(result);
        this.listener.setSuccess();
      }
    }
    else if (count == -1) {
      this.listener.finish();
      this.partitioner.interrupt();
    }
    else {
      this.listener.setFault(fault);
      this.partitioner.interrupt();
    }
  }

}
