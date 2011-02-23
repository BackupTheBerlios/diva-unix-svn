package diva.sca.extension.popup.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.stp.sca.diagram.edit.parts.ComponentEditPart;
import org.eclipse.stp.sca.impl.ComponentImpl;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import diva.sca.extension.jobs.CreateBundleProject;


public class GenerateComponentOSGIImplementation implements IObjectActionDelegate {

	private Shell shell;

	private ComponentImpl bundleComponent;

	/**
	 * Constructor for Action1.
	 */
	public GenerateComponentOSGIImplementation() {
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
		MessageDialog.openInformation(
				shell,
				"SCA to OSGi Factory Plug-in",
				"OSGi factory will be created for the "+bundleComponent.getName()+" component type"
		);

		if(Checker.check(shell, bundleComponent)){
			CreateBundleProject j = new CreateBundleProject("Create OSGi Factory for "+bundleComponent.getName());
			j.setBundleComponent(this.getBundleComponent());
			j.schedule();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		StructuredSelection selections = (StructuredSelection) selection;
		NodeImpl n = (NodeImpl) ((ComponentEditPart) selections.getFirstElement()).getModel();

		EObject obj = n.getElement();
		org.eclipse.stp.sca.impl.ComponentImpl component = (ComponentImpl) obj;
		this.setBundleComponent(component);
	}

	public ComponentImpl getBundleComponent() {
		return bundleComponent;
	}

	public void setBundleComponent(ComponentImpl bundleComponent) {
		this.bundleComponent = bundleComponent;
	}


}
