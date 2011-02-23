package org.kermeta.smartadapters.diva.toDrools.ui;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CleanerUI implements IObjectActionDelegate, Runnable {
	
	
	public static void main(String[] args){
		clean.Main.main(args);
	}
	
	protected StructuredSelection currentSelection;
    protected IFile file;
	   
	public void run() {
		//IOConsole console = new EclipseConsole("SmartAdapters Cleaner for DiVA");
		//console.println(new InfoMessage("Cleaning DiVA architectural model: " + file.getLocation().toOSString() + "..."));
		
		String file_uri = "file://" + file.getLocation().toOSString().replace("\\", "/");

		clean.Main.main(new String[]{file_uri});		
		//ScalaAspect.art.RichFactory.createMainCleaning().main(file_uri);
		
		
		//console.println(new InfoMessage("Done"));
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		new Thread(this).start();
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		
		if (selection instanceof StructuredSelection)
		{
			currentSelection = (StructuredSelection)selection;
			Iterator<StructuredSelection> it = currentSelection.iterator();

			while(it.hasNext()) {
				file = (IFile)it.next();
			}
		}
	}
}
