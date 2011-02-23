package diva.sca.extension.popup.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.diagram.edit.parts.CompositeEditPart;
import org.eclipse.stp.sca.impl.ComponentImpl;
import org.eclipse.stp.sca.impl.CompositeImpl;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import diva.sca.extension.jobs.CreateBundleProject;


public class GenerateAllComponentOSGIImplementations implements IObjectActionDelegate {

	private Shell shell;


	private CompositeImpl composite;

	/**
	 * Constructor for Action1.
	 */
	public GenerateAllComponentOSGIImplementations() {
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
		StructuredSelection selections = (StructuredSelection) selection;
		NodeImpl node = (NodeImpl) ((CompositeEditPart) selections.getFirstElement()).getModel();		
		EObject obj = node.getElement();
		composite = (CompositeImpl) obj;
	}

}
