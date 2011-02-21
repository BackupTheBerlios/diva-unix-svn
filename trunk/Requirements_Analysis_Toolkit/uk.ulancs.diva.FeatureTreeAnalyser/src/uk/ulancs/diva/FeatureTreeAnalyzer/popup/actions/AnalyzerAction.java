package uk.ulancs.diva.FeatureTreeAnalyzer.popup.actions;

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

import uk.ulancs.diva.FeatureTreeAnalyzer.Compare.Compare;

import ca.uwaterloo.gp.fmp.util.FmpExternalLoader;

public class AnalyzerAction implements IObjectActionDelegate {

	private Shell shell;
	private Vector<IFile> Trees;

	

	
	
	/**
	 * Constructor for Action1.
	 */
	public AnalyzerAction() {
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
		boolean TwoTrees = Trees.size() == 2;
		
		if (TwoTrees) {
			IFile TreeFile1 = Trees.get(0);
			IFile TreeFile2 = Trees.get(1);
			
			FmpExternalLoader Tree1 = new FmpExternalLoader();
			Tree1.load(TreeFile1);
			
			FmpExternalLoader Tree2 = new FmpExternalLoader();
			Tree2.load(TreeFile2);
			
			
			Compare Analyzer = new Compare();
			Analyzer.CompareTrees(Tree1,TreeFile1.getName(),Tree2,TreeFile2.getName(),"/home/joost/runtime-EclipseApplication(1)/TestProject/Mining Patterns/divacriterion.txt");
			MessageDialog.openInformation(
					shell,
					"FeatureTreeAnalyzer",
				"Similarity Analysis was executed.");
		}
		else {
			MessageDialog.openInformation(
					shell,
					"FeatureTreeAnalyzer",
				"Similarity Analysis could not be executed.");
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
