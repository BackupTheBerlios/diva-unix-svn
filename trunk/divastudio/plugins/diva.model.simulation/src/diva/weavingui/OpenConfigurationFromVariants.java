package diva.weavingui;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import diva.VariabilityModel;
import diva.Variant;
import fr.irisa.triskell.eclipse.console.EclipseConsole;

public class OpenConfigurationFromVariants implements IObjectActionDelegate, Runnable {

	protected StructuredSelection currentSelection;
    protected ArrayList<Variant> variants = new ArrayList<Variant>();
	
	public OpenConfigurationFromVariants() {
		super();
	}
	
	public void run() {
		System.out.println("Variants size : " + variants.size());
		
		VariabilityModel model = (VariabilityModel)variants.get(0).eResource().getContents().get(0);
		
		WeaveVariants.openConfiguration(model, variants, new EclipseConsole("DiVA"));
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
			variants.clear();
			currentSelection = (StructuredSelection)selection;
			Iterator it = currentSelection.iterator();

			while(it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof Variant) {
					variants.add((Variant)obj);
				}
			}

		}
		
	}
	
}