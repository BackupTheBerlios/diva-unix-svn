package de.pure.diva.arf.rc.reasoner.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.featuremodel.Feature;
import org.eclipse.variantmodel.SelectionState;
import org.eclipse.variantmodel.VariantSelection;

import arf.model.ARFConfiguration;
import arf.model.ARFContext;
import arf.model.ARFModel;
import arf.model.util.ARFModelConstants;
import arf.model.util.ARFModeling;
import arf.model.util.ARFModeling.Range;
import de.pure.diva.arf.alloy.sat.AlloySATSolver;
import de.pure.diva.arf.alloy.sat.util.ARFModel2AlloyGenerator;

public class RCSelector {
  public static int INFINITE_INSTANCES = -1;
  public static int INSTANCES          = INFINITE_INSTANCES;

  class Selector extends Thread {
    private RCSelectorSetting   setting     = RCSelectorSetting.DEFAULT;
    private RCSelectorDataStore store       = null;
    private ARFModel            model       = null;
    private ARFContext          context     = null;
    private int                 maxcount    = 0;
    private RCSelectorListener  listener    = null;
    private boolean             interrupted = false;

    private AlloySATSolver      solver      = null;

    public Selector(RCSelectorSetting setting, RCSelectorDataStore store, ARFModel model, ARFContext context, int maxcount, RCSelectorListener listener) {
      this.setting = setting;
      this.store = store;
      this.model = model;
      this.context = context;
      this.maxcount = maxcount;
      this.listener = listener;
      this.solver = new AlloySATSolver(this.setting.solvetimeout, this.setting.solvecount, AlloySATSolver.ALL);
      super.setName("RCSelector");
    }

    @Override
    public void interrupt() {
      this.interrupted = true;
      super.interrupt();
    }

    @Override
    public boolean isInterrupted() {
      return this.interrupted || super.isInterrupted();
    }

    @Override
    public void run() {
      try {
        if (this.model != null) {
          List<Feature> defaultSelected = new ArrayList<Feature>();
          List<Feature> defaultExcluded = new ArrayList<Feature>();
          Feature root = this.model.getRoot();
          defaultSelected.add(root);
          if (this.context != null) {
            for (VariantSelection selection : this.context.getSelections()) {
              Feature element = selection.getFeature();
              if (element != null) {
                if (SelectionState.SELECTED.getLiteral().equals(selection.getState().getLiteral()) == true) {
                  defaultSelected.add(element);
                }
                else {
                  defaultExcluded.add(element);
                }
              }
            }
          }
          select(this.model, maxcount, defaultSelected, defaultExcluded);
          this.listener.setSuccess();
        }
        else {
          this.listener.setFault("Application error:\n\nNo problem defined.");
        }
      }
      catch (Throwable e) {
        String msg = e.getMessage() == null ? "Unknown application error: " + e.getClass() : "Application error:\n\n" + e.getMessage();
        this.listener.setFault(msg);
      }
      RCSelector.this.selectors.remove(this.listener);
    }

    protected ARFConfiguration getConfiguration() {
      ARFConfiguration configuration = new ARFConfiguration();
      int random = getRandom(RANDOM_MAX);
      if (random <= RANDOM_MAX * this.setting.configurationrate / 100) {
        int number = this.store.getSatisfiableCount();
        if (number > 0) {
          int i = getRandom(this.store.getSatisfiableCount() - 1);
          ARFConfiguration tmp = this.store.getSatisfiable(i);
          if (tmp != null) {
            configuration = new ARFConfiguration(tmp);
          }
        }
      }
      return configuration;
    }

    private List<Feature> getSelectables(ARFConfiguration configuration, ARFModel model) {
      List<Feature> selectables = ARFModeling.getFeatures(model);
      for (VariantSelection selection : configuration.getSelections()) {
        Feature element = selection.getFeature();
        if (element != null) {
          selectables.remove(element);
        }
      }
      return selectables;
    }

    private List<Feature> getSelection(ARFConfiguration configuration, ARFModel model, List<Feature> defaultSelected, List<Feature> defaultExcluded,
        List<Feature> selectables) {
      List<Feature> selections = new ArrayList<Feature>();
      if (configuration.getSelections().isEmpty() == true) {
        for (Feature element : defaultSelected) {
          List<Feature> tmp = select(configuration, model, element, selectables);
          if (tmp == null) {
            selections = null;
            break;
          }
          else {
            selections.addAll(tmp);
          }
        }
        if (selections != null) {
          for (Feature element : defaultExcluded) {
            List<Feature> tmp = exclude(configuration, model, element, selectables);
            if (tmp == null) {
              selections = null;
              break;
            }
            else {
              selections.addAll(tmp);
            }
          }
        }
      }
      return selections;
    }

    private void select(ARFModel model, int maxcount, List<Feature> defaultSelected, List<Feature> defaultExcluded) {
      if (maxcount > 0 || maxcount == -1) {
        int count = this.listener.count();
        while (this.isInterrupted() == false && ((maxcount > count) || (maxcount == -1))) {
          ARFConfiguration configuration = getConfiguration();
          ARFModeling.setModelName(configuration, ARFModelConstants.CONFIGURATION_NAME + count);
          List<Feature> selectables = getSelectables(configuration, model);
          List<Feature> selections = getSelection(configuration, model, defaultSelected, defaultExcluded, selectables);
          if (selections != null) {
            boolean result = next(configuration, model, selectables);
            if (result == true) {
              addConfiguration(configuration);
              if (isReady() == true) {
                selectables.clear();
              }
            }
          }
          count = this.listener.count();
        }
      }
    }

    protected synchronized void addConfiguration(ARFConfiguration configuration) {
      String hash = this.store.getHash(configuration);
      boolean added = this.store.addSatisfiable(hash, configuration);
      if (added == true) {
        this.listener.addConfiguration(configuration);
        RCSelector.this.increment();
      }
    }

    protected synchronized boolean isReady() {
      return ((this.isInterrupted() == true) || (((this.listener.count() >= this.maxcount)) && (this.maxcount != -1)));
    }

    private boolean select(ARFConfiguration configuration, ARFModel model, Feature element, List<Feature> selectables, int steps) {
      boolean result = true;
      if (steps > 1 || this.setting.steps == RCSelectorSetting.INFINITE_STEPS) {
        result = selectNext(configuration, model, element, selectables, steps - 1);
      }
      else {
        result = selectStep(configuration, model, element, selectables);
      }
      return result;
    }

    private boolean selectNext(ARFConfiguration configuration, ARFModel model, Feature element, List<Feature> selectables, int steps) {
      List<Feature> selections = select(configuration, model, element, selectables);
      boolean result = true;
      if (selections != null) {
        if (next(configuration, model, selectables, steps) == false) {
          result = unselect(configuration, selections, selectables);
        }
        if (result == true) {
          if (selections.isEmpty() == true) {
            if (((steps + 1) == this.setting.steps && this.setting.backtrace == RCSelectorSetting.BACKTRACE_TO_STEPONE) || this.setting.backtrace == RCSelectorSetting.BACKTRACE_TO_MINUSONE) {
              selections = exclude(configuration, model, element, selectables);
              if (selections != null) {
                if (next(configuration, model, selectables, steps) == false) {
                  result = unselect(configuration, selections, selectables);
                }
                if (result == true && selections.isEmpty() == true) {
                  // element can not be selected or excluded
                  result = false;
                }
              }
              else {
                // error in selection
                result = false;
              }
            }
            else {
              // element may not be excluded
              result = false;
            }
          }
        }
      }
      else {
        // error in selection
        result = false;
      }
      return result;
    }

    private boolean selectStep(ARFConfiguration configuration, ARFModel model, Feature element, List<Feature> selectables) {
      List<Feature> selections = select(configuration, model, element, selectables);
      boolean result = true;
      if (selections != null) {
        if (isSatifiable(configuration, model, selectables) == false) {
          result = unselect(configuration, selections, selectables);
        }
        if (result == true) {
          if (selections.isEmpty() == true) {
            if (1 == this.setting.steps || this.setting.backtrace == RCSelectorSetting.BACKTRACE_AT_STEPMAX || this.setting.backtrace == RCSelectorSetting.BACKTRACE_TO_MINUSONE) {
              selections = exclude(configuration, model, element, selectables);
              if (selections != null) {
                if (isSatifiable(configuration, model, selectables) == false) {
                  result = unselect(configuration, selections, selectables);
                }
                if (result == true && selections.isEmpty() == true) {
                  // element can not be selected or excluded
                  result = false;
                }
              }
              else {
                // error in selection
                result = false;
              }
            }
            else {
              // element may not be excluded
              result = false;
            }
          }
          if (result == true) {
            result = next(configuration, model, selectables);
          }
        }
      }
      else {
        // error in selection
        result = false;
      }
      return result;
    }

    private boolean exclude(ARFConfiguration configuration, ARFModel model, Feature element, List<Feature> selectables, int steps) {
      boolean result = true;
      if (steps > 1 || this.setting.steps == RCSelectorSetting.INFINITE_STEPS) {
        result = excludeNext(configuration, model, element, selectables, steps - 1);
      }
      else {
        result = excludeStep(configuration, model, element, selectables);
      }
      return result;
    }

    private boolean excludeNext(ARFConfiguration configuration, ARFModel model, Feature element, List<Feature> selectables, int steps) {
      List<Feature> selections = exclude(configuration, model, element, selectables);
      boolean result = true;
      if (selections != null) {
        if (next(configuration, model, selectables, steps) == false) {
          result = unselect(configuration, selections, selectables);
        }
        if (result == true) {
          if (selections.isEmpty() == true) {
            if (((steps + 1) == this.setting.steps && this.setting.backtrace == RCSelectorSetting.BACKTRACE_TO_STEPONE) || this.setting.backtrace == RCSelectorSetting.BACKTRACE_TO_MINUSONE) {
              selections = select(configuration, model, element, selectables);
              if (selections != null) {
                if (next(configuration, model, selectables, steps) == false) {
                  result = unselect(configuration, selections, selectables);
                }
                if (result == true && selections.isEmpty() == true) {
                  // element can not be selected or excluded
                  result = false;
                }
              }
              else {
                // error in selection
                result = false;
              }
            }
            else {
              // element may not be selected
              result = false;
            }
          }
        }
      }
      else {
        // error in selection
        result = false;
      }
      return result;
    }

    private boolean excludeStep(ARFConfiguration configuration, ARFModel model, Feature element, List<Feature> selectables) {
      List<Feature> selections = exclude(configuration, model, element, selectables);
      boolean result = true;
      if (selections != null) {
        if (isSatifiable(configuration, model, selectables) == false) {
          result = unselect(configuration, selections, selectables);
        }
        if (result == true) {
          if (selections.isEmpty() == true) {
            if (1 == this.setting.steps || this.setting.backtrace == RCSelectorSetting.BACKTRACE_AT_STEPMAX || this.setting.backtrace == RCSelectorSetting.BACKTRACE_TO_MINUSONE) {
              selections = select(configuration, model, element, selectables);
              if (selections != null) {
                if (isSatifiable(configuration, model, selectables) == false) {
                  result = unselect(configuration, selections, selectables);
                }
                if (result == true && selections.isEmpty() == true) {
                  // element can not be excluded or selected
                  result = false;
                }
              }
              else {
                result = false;
                // error in selection
              }
            }
            else {
              // element may not be selected
              result = false;
            }
          }
          if (result == true) {
            result = next(configuration, model, selectables);
          }
        }
      }
      else {
        // error in selection
        result = false;
      }
      return result;
    }

    private boolean unselect(ARFConfiguration configuration, List<Feature> selections, List<Feature> selectables) {
      boolean result = true;
      if (selections.isEmpty() == false) {
        for (Feature selection : selections) {
          VariantSelection tmp = null;
          for (VariantSelection vs : configuration.getSelections()) {
            if (ARFModeling.getElementName(vs).equals(selection.getName()) == true) {
              tmp = vs;
              break;
            }
          }
          if (tmp != null) {
            configuration.getSelections().remove(tmp);
          }
          if (selectables.contains(selection) == false) {
            selectables.add(selection);
          }
        }
        selections.clear();
      }
      else {
        // no more previously select-able elements
        result = false;
      }
      return result;
    }

    private boolean next(ARFConfiguration configuration, ARFModel model, List<Feature> selectables) {
      return next(configuration, model, selectables, this.setting.steps);
    }

    private boolean next(ARFConfiguration configuration, ARFModel model, List<Feature> selectables, int steps) {
      boolean result = true;
      if (selectables.isEmpty() == true) {
        // no more select-able elements
        if (steps != this.setting.steps) {
          result = isSatifiable(configuration, model, selectables);
        }
      }
      else if (this.isInterrupted() == true) {
        result = false;
        selectables.clear();
      }
      else {
        Feature element = getElement(selectables);
        int i = getRandom(RANDOM_MAX);
        if (i <= RANDOM_MAX * this.setting.selectrate / 100) {
          result = select(configuration, model, element, selectables, steps);
        }
        else {
          result = exclude(configuration, model, element, selectables, steps);
        }
      }
      return result;
    }

    private boolean isSatifiable(ARFConfiguration configuration, ARFModel model, List<Feature> selectables) {
      boolean result = true;
      String hash = this.store.getHash(configuration);
      if (this.store.isUnsatisfiable(hash) == true) {
        result = false;
      }
      else if (this.store.isSatisfiable(hash) == true) {}
      else {
        if (this.store.isPartOfSatisfiable(configuration) == true) {
          // satisfiable
          this.store.addSatisfiable(hash, new ARFConfiguration(configuration));
        }
        else {
          AllAlloyAtomsListener listener = new AllAlloyAtomsListener(this, configuration, this.solver.connector, model);
          ARFModel2AlloyGenerator aGenerator = new ARFModel2AlloyGenerator(configuration);
          String system = aGenerator.generate(model);
          String context = aGenerator.select(model, configuration);
          try {
            this.solver.solve(system, context, listener);
            this.solver.sychronise(listener);
          }
          catch (Throwable e) {}

          if (listener.satisfiable() == false) {
            // not satisfiable
            this.store.addUnsatisfiable(hash);
            result = false;
          }
          else {
            // satisfiable
            this.store.addSatisfiable(hash, new ARFConfiguration(configuration));

            if (isReady() == true) {
              selectables.clear();
            }
          }
        }
      }
      return result;
    }

    private List<Feature> select(ARFConfiguration configuration, ARFModel model, Feature element, List<Feature> selectables) {
      List<Feature> result = null;
      String name = element.getName();
      if (ARFModeling.isSelection(configuration, model, name) == false || ARFModeling.isSelected(configuration, model, name) == true) {
        result = new ArrayList<Feature>();
      }
      if (result != null) {
        if (ARFModeling.isSelection(configuration, model, name) == false) {
          select(configuration, name, model);
          result.add(element);
          selectables.remove(element);
          Feature parent = ARFModeling.getParent(element);
          if (parent != null) {
            // select parent implicitly
            List<Feature> presult = select(configuration, model, parent, selectables);
            if (presult != null) {
              result.addAll(presult);
            }
            else {
              result = null;
            }
            Map<String, List<Feature>> children = ARFModeling.getChildren(parent);
            // exclude alternative siblings implicitly
            List<Feature> alternatives = children.get(ARFModelConstants.ALTERNATIVE);
            if (alternatives.contains(element) == true) {
              for (Iterator<Feature> idx = alternatives.iterator(); result != null && idx.hasNext() == true;) {
                Feature alternative = idx.next();
                if (element != alternative) {
                  // exclude alternative sibling implicitly
                  List<Feature> aresult = exclude(configuration, model, alternative, selectables);
                  if (aresult != null) {
                    result.addAll(aresult);
                  }
                  else {
                    result = null;
                  }
                }
              }
            }
            // exclude or siblings implicitly, if range is satisfied
            List<Feature> ors = children.get(ARFModelConstants.OR);
            if (ors.contains(element) == true) {
              List<Feature> selections = getSelected(configuration, model, ors);
              Range range = ARFModeling.getRange(parent);
              if (range.upper != Range.UNBOUNDED && range.upper == selections.size()) {
                for (Iterator<Feature> idx = ors.iterator(); result != null && idx.hasNext() == true;) {
                  Feature or = idx.next();
                  if (selections.contains(or) == false) {
                    // exclude or sibling implicitly
                    List<Feature> oresult = exclude(configuration, model, or, selectables);
                    if (oresult != null) {
                      result.addAll(oresult);
                    }
                    else {
                      result = null;
                    }
                  }
                }
              }
            }
          }
          Map<String, List<Feature>> children = ARFModeling.getChildren(element);
          // select mandatory children implicitly
          List<Feature> mandatories = children.get(ARFModelConstants.MANDATORY);
          for (Iterator<Feature> idx = mandatories.iterator(); result != null && idx.hasNext() == true;) {
            Feature mandatory = idx.next();
            // select mandatory child implicitly
            List<Feature> mresult = select(configuration, model, mandatory, selectables);
            if (mresult != null) {
              result.addAll(mresult);
            }
            else {
              result = null;
            }
          }
        }
      }
      return result;
    }

    private List<Feature> exclude(ARFConfiguration configuration, ARFModel model, Feature element, List<Feature> selectables) {
      List<Feature> result = null;
      String name = element.getName();
      if (ARFModeling.isSelection(configuration, model, name) == false || ARFModeling.isSelected(configuration, model, name) == false) {
        result = new ArrayList<Feature>();
      }
      if (result != null) {
        if (ARFModeling.isSelection(configuration, model, name) == false) {
          exclude(configuration, name, model);
          result.add(element);
          selectables.remove(element);
          Feature parent = ARFModeling.getParent(element);
          if (parent != null) {
            Map<String, List<Feature>> children = ARFModeling.getChildren(parent);
            // select last alternative sibling implicitly
            List<Feature> alternatives = children.get(ARFModelConstants.ALTERNATIVE);
            if (alternatives.contains(element) == true) {
              List<Feature> exclusions = getExcluded(configuration, model, alternatives);
              if (alternatives.size() == exclusions.size() + 1) {
                for (Iterator<Feature> idx = alternatives.iterator(); result != null && idx.hasNext() == true;) {
                  Feature alternative = idx.next();
                  if (exclusions.contains(alternative) == false) {
                    List<Feature> aresult = null;
                    if (ARFModeling.isSelection(configuration, model, parent.getName()) == true && ARFModeling.isSelected(configuration, model, parent
                        .getName()) == false) {
                      // exclude last alternative sibling implicitly
                      aresult = exclude(configuration, model, alternative, selectables);
                    }
                    else {
                      // select last alternative sibling implicitly
                      aresult = select(configuration, model, alternative, selectables);
                    }
                    if (aresult != null) {
                      result.addAll(aresult);
                    }
                    else {
                      result = null;
                    }
                  }
                }
              }
            }
            // select or siblings implicitly, if range can be satisfied
            List<Feature> ors = children.get(ARFModelConstants.OR);
            if (ors.contains(element) == true) {
              List<Feature> exclusions = getExcluded(configuration, model, ors);
              Range range = ARFModeling.getRange(parent);
              if (range.lower > 0 && ors.size() == exclusions.size() + range.lower) {
                for (Iterator<Feature> idx = ors.iterator(); result != null && idx.hasNext() == true;) {
                  Feature or = idx.next();
                  if (exclusions.contains(or) == false) {
                    List<Feature> oresult = null;
                    if (ARFModeling.isSelection(configuration, model, parent.getName()) == true && ARFModeling.isSelected(configuration, model, parent
                        .getName()) == false) {
                      // exclude last or siblings implicitly
                      oresult = exclude(configuration, model, or, selectables);
                    }
                    else {
                      // select last or siblings implicitly
                      oresult = select(configuration, model, or, selectables);
                    }
                    if (oresult != null) {
                      result.addAll(oresult);
                    }
                    else {
                      result = null;
                    }
                  }
                }
              }
            }
          }
          Map<String, List<Feature>> children = ARFModeling.getChildren(element);
          List<Feature> all = new ArrayList<Feature>();
          all.addAll(children.get(ARFModelConstants.MANDATORY));
          all.addAll(children.get(ARFModelConstants.OPTIONAL));
          all.addAll(children.get(ARFModelConstants.ALTERNATIVE));
          all.addAll(children.get(ARFModelConstants.OR));
          // exclude all children implicitly
          for (Iterator<Feature> idx = all.iterator(); result != null && idx.hasNext() == true;) {
            Feature child = idx.next();
            // exclude every child implicitly
            List<Feature> cresult = exclude(configuration, model, child, selectables);
            if (cresult != null) {
              result.addAll(cresult);
            }
            else {
              result = null;
            }
          }
        }
      }
      return result;
    }

    private void select(ARFConfiguration configuration, String name, ARFModel model) {
      ARFModeling.addSelection(configuration, name, model, SelectionState.SELECTED);
    }

    private void exclude(ARFConfiguration configuration, String name, ARFModel model) {
      ARFModeling.addSelection(configuration, name, model, SelectionState.EXCLUDED);
    }

    private List<Feature> getSelected(ARFConfiguration configuration, ARFModel model, List<Feature> elements) {
      List<Feature> selections = new ArrayList<Feature>();
      for (Feature element : elements) {
        String name = element.getName();
        if (ARFModeling.isSelected(configuration, model, name) == true) {
          selections.add(element);
        }
      }
      return selections;
    }

    private List<Feature> getExcluded(ARFConfiguration configuration, ARFModel model, List<Feature> elements) {
      List<Feature> selections = new ArrayList<Feature>();
      for (Feature element : elements) {
        String name = element.getName();
        if (ARFModeling.isExcluded(configuration, model, name) == true) {
          selections.add(element);
        }
      }
      return selections;
    }

    private Feature getElement(List<Feature> elements) {
      Feature element = null;
      if (elements.isEmpty() == false) {
        int index = getRandom(elements.size() - 1);
        element = elements.get(index);
      }
      return element;
    }

    private int getRandom(int index) {
      int num = (index - 1 + RANDOM_MAX) / RANDOM_MAX;
      int max = num * (RANDOM_MAX + 1) - 1;
      SecureRandom random = new SecureRandom();
      byte[] res = new byte[num];
      random.nextBytes(res);
      char sum = 0;
      for (byte b : res) {
        sum += b & 0x7f;
      }
      int result = index * sum / max;
      return result;
    }
  }

  private static final int                       RANDOM_MAX = 127;

  private RCSelectorSetting                      setting    = RCSelectorSetting.DEFAULT;

  private int                                    count      = -1;
  private Map<RCSelectorListener, Set<Selector>> selectors  = Collections.synchronizedMap(new HashMap<RCSelectorListener, Set<Selector>>());

  public RCSelector() {
    this(RCSelectorSetting.DEFAULT);
  }

  public RCSelector(RCSelectorSetting setting) {
    this.setting = setting;
  }

  public synchronized int count() {
    return this.count;
  }

  private synchronized void increment() {
    this.count++;
  }

  public void cancel(RCSelectorListener listener) {
    Set<Selector> parallel = this.selectors.get(listener);
    if (parallel != null) {
      synchronized (parallel) {
        for (Selector selector : parallel) {
          selector.interrupt();
        }
        parallel.clear();
        this.selectors.remove(listener);
      }
    }
  }

  public void select(ARFModel model, int maxcount, RCSelectorListener listener) {
    select(model, null, maxcount, listener);
  }

  public void select(ARFModel model, ARFContext context, int maxcount, RCSelectorListener listener) {
    this.count = 0;
    int number = RCSelector.INSTANCES;
    if (number == RCSelector.INFINITE_INSTANCES) {
      number = 1;
    }
    Set<Selector> parallel = Collections.synchronizedSet(new HashSet<Selector>());
    RCSelectorDataStore store = new RCSelectorDataStore();
    for (int i = 0; i < number; ++i) {
      Selector selector = new Selector(this.setting, store, model, context, maxcount, listener);
      parallel.add(selector);
      selector.start();
      try {
        Thread.sleep(20);
      }
      catch (InterruptedException e) {}
    }
    this.selectors.put(listener, parallel);
  }
}
