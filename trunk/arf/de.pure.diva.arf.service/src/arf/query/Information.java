package arf.query;

import java.util.ArrayList;
import java.util.List;

import service.arf.reasoner.Reasoner;
import service.arf.reasoner.ReasonerHandle;

public class Information implements service.arf.query.Information {

  private List<ReasonerHandle> reasoner = new ArrayList<ReasonerHandle>();

  public List<ReasonerHandle> getReasoner() {
    return this.reasoner;
  }

  public void addReasoner(Reasoner reasoner) {
    this.reasoner.add(new arf.reasoner.ReasonerHandle(reasoner.getName()));
  }

}
