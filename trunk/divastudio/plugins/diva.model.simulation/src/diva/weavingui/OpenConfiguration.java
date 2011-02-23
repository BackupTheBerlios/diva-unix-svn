package diva.weavingui;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import diva.ConfigVariant;
import diva.Configuration;
import diva.VariabilityModel;
import diva.Variant;
import fr.irisa.triskell.eclipse.console.EclipseConsole;

public class OpenConfiguration implements IObjectActionDelegate, Runnable {

	protected StructuredSelection currentSelection;
    protected Configuration config;
	
	public OpenConfiguration() {
		super();
	}
	
	public void run() {
	
		if (config == null) return;
		
		VariabilityModel model = (VariabilityModel)config.eResource().getContents().get(0);
		
		ArrayList<Variant> variants = new ArrayList<Variant>();
		for(ConfigVariant cv : config.getVariant()) {
			variants.add(cv.getVariant());
		}
		
		EclipseConsole c = new EclipseConsole("DiVA");
		
		WeaveVariants.openConfiguration(model, variants, c);
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
			Iterator it = currentSelection.iterator();

			while(it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof Configuration) {
					config = (Configuration)obj;
				}
			}

		}
		
	}
	
}