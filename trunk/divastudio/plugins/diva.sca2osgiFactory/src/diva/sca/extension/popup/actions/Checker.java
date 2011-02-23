package diva.sca.extension.popup.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.swt.widgets.Shell;

public class Checker {

	public static boolean check(Shell shell, Component c){
		boolean check = true;

		if(c.getConstrainingType() == null){
			check = false;
			MessageDialog.openError(shell, "Error", "No Constraining Type Specified for "+c.getName());
		}

		if(c.getName() == null){
			check = false;
			MessageDialog.openError(shell, "Error", "No Name Specified for "+c.getName());
		}

		if(c.getImplementation() == null){
			check = false;
			MessageDialog.openError(shell, "Error", "No Implementation Class Specified for "+c.getName());
		} 
		else {
			if(! (c.getImplementation() instanceof JavaImplementation)){
				check = false;
				MessageDialog.openError(shell, "Error", "Implementation Class is not a Java Implementation for "+c.getName());
			}
			else if( ((JavaImplementation)c.getImplementation()).getClass_() == null){
				check = false;
				MessageDialog.openError(shell, "Error", "No Class is specified for the Java Implementation of "+c.getName());
			}
		}

		for(ComponentService s : c.getService()){
			if(s.getInterface() == null){
				check = false;
				MessageDialog.openError(shell, "Error", "No interface for service "+s.getName());
			}
		}
		
		for(ComponentReference s : c.getReference()){
			if(s.getInterface() == null){
				check = false;
				MessageDialog.openError(shell, "Error", "No interface for service "+s.getName());
			}
		}
		
		return check;
	}
	
}
