package uk.ulancs.diva.traceartifacts.pop.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import ca.uwaterloo.gp.fmp.Project;
import ca.uwaterloo.gp.fmp.Feature;
import ca.uwaterloo.gp.fmp.Node;
import ca.uwaterloo.gp.fmp.TypedValue;
import ca.uwaterloo.gp.fmp.presentation.FmpEditor;
import diva.NamedElement;
import diva.presentation.DivaEditor;

public class DiVATraceArtifactsAction implements IEditorActionDelegate {
	
	private Shell shell;
	
	public String getName(Node n){
		Feature properties= n.getProperties();
		
		if(properties!=null){
			if(properties.getChildren().get(0) instanceof Feature){
		Object property= ((Feature)properties.getChildren().get(0)).eAdapters().get(0).getTarget();
		if(property instanceof TypedValue){
			return ((TypedValue)property).getStringValue();
		}
			}
		}
		return null;
	}

	public void run(IAction action) {
		DivaEditor editor1= (DivaEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		editor1.setFocus();
		
		IFileEditorInput input= (IFileEditorInput)editor1.getEditorInput();
		
		IFile divaFile= input.getFile();
		String[] fullpath= divaFile.getFullPath().segments();
		
		String fmpFile= "";
		
		for(int i=1; i<fullpath.length;i++){
			fmpFile= fmpFile+fullpath[i]+"/";
		}
		
		fmpFile=fmpFile.substring(0,fmpFile.length()-1);
		
		IProject project= input.getFile().getProject();
		
		
		IFile file= project.getFile(fmpFile.replaceAll(".diva", ""));
		
		TreeSelection divaselection= (TreeSelection)editor1.getSelection();
		
		boolean found=false;
		
		if(divaselection.getFirstElement() instanceof NamedElement){
		
		NamedElement selected_element= (NamedElement)divaselection.getFirstElement();
		String selected_element_name= selected_element.getName();	
		
		
		IFileEditorInput fmpInput= new FileEditorInput(file);
		
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(fmpInput, "ca.uwaterloo.gp.fmp.FmpEditor");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FmpEditor editor= (FmpEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		editor.setFocus();		
		
		for(TreeIterator<EObject> it= ((Project)editor.getEditingDomain().getResourceSet().getResources().get(0).getContents().get(0)).eAllContents();it.hasNext();){
			EObject object= it.next();
			if(object instanceof Node){
				String name= getName((Node)object);
				if(name!=null&&name.equals(selected_element_name)){
					ISelection selection = new StructuredSelection(object);
					editor.setSelection(selection);
					editor.getViewer().setSelection(selection);
					found=true;
				}
			}
		}
		}
		
		if(!found){
			MessageDialog.openInformation(
					shell,
					"TraceArtifacts",
					"Cannot find model element.");
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}


	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		shell = targetEditor.getSite().getShell();
		
	}

}
