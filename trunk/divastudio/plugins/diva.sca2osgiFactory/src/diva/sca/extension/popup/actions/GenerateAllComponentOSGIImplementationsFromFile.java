package diva.sca.extension.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.impl.ComponentImpl;
import org.eclipse.stp.sca.impl.CompositeImpl;
import org.eclipse.stp.sca.impl.DocumentRootImpl;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import diva.sca.extension.jobs.CreateBundleProject;


public class GenerateAllComponentOSGIImplementationsFromFile implements IObjectActionDelegate {

	private Shell shell;


	private CompositeImpl composite;
	private IFile file;

	/**
	 * Constructor for Action1.
	 */
	public GenerateAllComponentOSGIImplementationsFromFile() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		String message = new String();
		message += ("OSGi factories will be created for all the sub-components of "+composite.getName()+":");
		for(Component c : composite.getComponent()){
			message += ("\n\t -"+c.getName());
		}

		MessageDialog.openInformation(
				shell,
				"SCA to OSGi Factory Plug-in",
				message
		);

		for(Component c : composite.getComponent()){
			if(Checker.check(shell, c)){
				CreateBundleProject j = new CreateBundleProject("Create OSGi Factory for "+c.getName());
				j.setBundleComponent((ComponentImpl) c);
				j.schedule();
			}
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		//System.out.println("Selection: "+selection);

		StructuredSelection currentSelection;

		if (selection instanceof StructuredSelection)
		{
			// the selection should be a single *.composite file
			currentSelection = (StructuredSelection)selection;
			file = (IFile) currentSelection.getFirstElement();
		}		

		// Create a resource set.
		ResourceSet resourceSet = new ResourceSetImpl();
		// Register the default resource factory -- only needed for
		// stand-alone!
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION,
				new org.eclipse.stp.sca.util.ScaResourceFactoryImpl());
		// Get the URI of the model file.

		//System.out.println(file.getLocationURI().toString().substring(6));

		URI fileURI = URI.createFileURI(file.getLocationURI().toString().substring(6));
		// Demand load the resource for this file.
		Resource resource = resourceSet.getResource(fileURI, true);



		composite = (CompositeImpl)((DocumentRootImpl) resource.getContents().get(0)).getComposite();



	}

}
