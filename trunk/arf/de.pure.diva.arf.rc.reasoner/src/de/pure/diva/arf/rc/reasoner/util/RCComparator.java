package de.pure.diva.arf.rc.reasoner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.variantmodel.VariantSelection;

import arf.model.ARFConfiguration;
import arf.model.util.ARFModeling;

public class RCComparator implements Comparator<String> {

  public boolean contains(List<ARFConfiguration> configurations, ARFConfiguration configuration) {
    boolean result = false;
    for (ARFConfiguration c : configurations) {
      if (compare(configuration, c) == true) {
        result = true;
        break;
      }
    }
    return result;
  }

  public boolean compare(ARFConfiguration c1, ARFConfiguration c2) {
    List<String> l1 = getSelections(c1);
    List<String> l2 = getSelections(c2);
    boolean result = (l1.size() == l2.size()) && (l1.containsAll(l2) == true);
    return result;
  }

  public boolean part(Collection<ARFConfiguration> configurations, ARFConfiguration configuration) {
    boolean result = false;
    for (ARFConfiguration c : configurations) {
      if (part(c, configuration) == true) {
        result = true;
        break;
      }
    }
    return result;
  }

  public boolean part(ARFConfiguration c1, ARFConfiguration c2) {
    List<String> l1 = getSelections(c1);
    List<String> l2 = getSelections(c2);
    l1.retainAll(l2);
    return l1.size() == l2.size();
  }

  private List<String> getSelections(ARFConfiguration configuration) {
    List<String> list = new ArrayList<String>();
    for (VariantSelection selection : configuration.getSelections()) {
      String name = ARFModeling.getElementName(selection);
      String type = selection.getState().getLiteral();
      list.add(name + "#" + type);
    }
    return list;
  }

  public String toString(ARFConfiguration configuration) {
    List<String> list = getSelections(configuration);
    Collections.sort(list, this);
    return list.toString();
  }

  public int compare(String o1, String o2) {
    return o1.compareTo(o2);
  }

}
