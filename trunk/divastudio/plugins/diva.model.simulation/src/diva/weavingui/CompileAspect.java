package diva.weavingui;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import diva.Variant;
import fr.irisa.triskell.eclipse.console.EclipseConsole;
import fr.irisa.triskell.eclipse.console.messages.ErrorMessage;

public class CompileAspect implements IObjectActionDelegate, Runnable {

	protected StructuredSelection currentSelection;
    protected Variant variant;
	
	public CompileAspect() {
		super();
	}
	
	public void run() {
	
		EclipseConsole c = new EclipseConsole("DiVA");
		
		if (variant == null || variant.getModel() == null ||  variant.getModel().getUri().equals("")) {
			c.println(new ErrorMessage("ERROR: No Aspect is associated with this variant."));
			return;
		}
		
		final IFile aspect = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(URI.createURI(variant.getModel().getUri(), true).toPlatformString(true)));
		
		if (!aspect.exists()) {
			c.println(new ErrorMessage("ERROR: The aspect associated with this Variant does not exist."));
			return;
		}
		
		CompileAllAspects.compileVariantAspect(variant, c);
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
				if (obj instanceof Variant) {
					variant = (Variant)obj;
				}
			}

		}
		
	}
	
}