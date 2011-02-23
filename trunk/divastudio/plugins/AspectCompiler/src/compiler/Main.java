package compiler;

import runner.MainRunner;

public class Main {

	public static void main(String[] args) {
		//System.out.println("Cleaning: START");
		//En standalone
		MainRunner.main(args);

		//Dans eclipse
		//ScalaAspect.smartadapters4ART.RichFactory.createMain().main(args[0]);
		
		//System.out.println("Cleaning: STOP");
	}
}
