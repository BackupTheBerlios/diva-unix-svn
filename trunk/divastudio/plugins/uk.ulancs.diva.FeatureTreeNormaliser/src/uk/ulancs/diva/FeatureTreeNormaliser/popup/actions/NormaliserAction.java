package uk.ulancs.diva.FeatureTreeNormaliser.popup.actions;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import uk.ulancs.diva.FeatureTreeNormaliser.Normaliser.Normaliser;

import ca.uwaterloo.gp.fmp.util.FmpExternalLoader;

public class NormaliserAction implements IObjectActionDelegate {

	private Shell shell;
	private Vector<IFile> Trees;

	

	
	
	/**
	 * Constructor for Action1.
	 */
	public NormaliserAction() {
		super();
		Trees = new Vector<IFile>();
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
		boolean OneTree = Trees.size() == 1;
		
		if (OneTree) {
			IFile TreeFile = Trees.get(0);			
			FmpExternalLoader Tree = new FmpExternalLoader();
			Tree.load(TreeFile);			
			
			Normaliser TreeNorm = new Normaliser();
			TreeNorm.NormaliseTree(Tree);
			Tree.save(0);
			MessageDialog.openInformation(
					shell,
					"FeatureTreeNormaliser",
				"Feature tree has been normalised.");
		}
		else {
			MessageDialog.openInformation(
					shell,
					"FeatureTreeNormaliser",
				"Feature tree could not be normalised.");
		}
		
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		Iterator<IFile> it= ((StructuredSelection) selection).iterator();
		
		while(it.hasNext()){
			Trees.add(it.next());
		}
	}

}
