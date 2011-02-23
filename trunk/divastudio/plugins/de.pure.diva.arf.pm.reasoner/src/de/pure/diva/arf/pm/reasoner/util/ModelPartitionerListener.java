package de.pure.diva.arf.pm.reasoner.util;

import arf.SolverListener;
import arf.model.ARFConfiguration;

public class ModelPartitionerListener extends SolverListener {

  private ModelPartitioner partitioner = null;

  public ModelPartitionerListener(ModelPartitioner partitioner) {
    this.partitioner = partitioner;
  }

  public void addConfiguration(ARFConfiguration configuration) {
    handleConfiguration(configuration);
  }

  protected void handleConfiguration(ARFConfiguration configuration) {}

  public void synchronise(long timeout) throws Exception {
    super.synchronise("ModelPartitioner::Timeouter", timeout);
  }

  synchronized protected void finish() {
    if (hasResult() == false) {
      this.partitioner.cancel(this);
    }
    super.finish();
  }

  public int count() {
    int count = this.partitioner.count();
    if (hasSuccess() == false) {
      count = 0;
    }
    return count;
  }

}
