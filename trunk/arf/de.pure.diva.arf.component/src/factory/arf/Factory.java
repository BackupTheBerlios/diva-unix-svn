package factory.arf;

/**
 * This file was generated using DiVA Studio. Visit http://www.ict-diva.eu/ for
 * more details about DiVA.
 */
public class Factory implements eu.diva.factoryinstdiva.Factory<arf.component.ARF> {

  private static Factory fact = new Factory();

  public static Factory getFact() {
    return fact;
  }

  public static void setFact(Factory fact) {
    Factory.fact = fact;
  }

  public arf.component.ARF createComponent() {
    return new arf.component.ARF();

  }

  public arf.component.ARF createComponent(String implementingClass) {
    /*
     * if (check(implementingClass)) return new arf.ARF();//Return the right
     * implementing class else
     */
    return createComponent();// return the default implementing class
  }

  public boolean check(String implementingClass) {
    try {
      Class<?> c = Class.forName(implementingClass);
      c.asSubclass(arf.component.ARF.class);
      return true;
    }
    catch (ClassNotFoundException e) {
      // e.printStackTrace();
    }
    catch (ClassCastException e) {
      // e.printStackTrace();
    }
    return false;
  }

}