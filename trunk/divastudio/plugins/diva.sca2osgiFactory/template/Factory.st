package factory;

/**
* This file was generated using DiVA Studio.
* Visit http://www.ict-diva.eu/ for more details about DiVA.
*/
public class Factory implements eu.diva.factoryinstdiva.Factory<$ComponentImplClass$>{

	private static Factory fact = new Factory();

	public static Factory getFact() {
		return fact;
	}

	public static void setFact(Factory fact) {
		Factory.fact = fact;
	}

	public $ComponentImplClass$ createComponent() {
		return new $ComponentImplClass$();
		
	}
	
	public $ComponentImplClass$ createComponent(String implementingClass) {
		/*if (check(implementingClass))
			return new $ComponentImplClass$();//Return the right implementing class	
		else*/
			return createComponent();//return the default implementing class
	}

	public boolean check(String implementingClass) {
		try {
			Class<?> c = Class.forName(implementingClass);
			c.asSubclass($ComponentImplClass$.class);
			return true;
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
		} catch (ClassCastException e){
			//e.printStackTrace();
		}		
		return false;
	}

}
