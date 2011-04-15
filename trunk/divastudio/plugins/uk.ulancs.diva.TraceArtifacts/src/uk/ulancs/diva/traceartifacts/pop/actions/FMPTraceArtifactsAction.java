package uk.ulancs.diva.traceartifacts.pop.actions;

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

import bin.ca.uwaterloo.gp.fmp.Project;
import ca.uwaterloo.gp.fmp.Feature;
import ca.uwaterloo.gp.fmp.Node;
import ca.uwaterloo.gp.fmp.TypedValue;
import ca.uwaterloo.gp.fmp.presentation.FmpEditor;
import org.eclipse.core.resources.IFile;
import diva.VariabilityModel;
import diva.NamedElement;

import diva.presentation.DivaEditor;

public class FMPTraceArtifactsAction implements IEditorActionDelegate {
	
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
		FmpEditor editor1= (FmpEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		editor1.setFocus();
		
		IFileEditorInput input= (IFileEditorInput)editor1.getEditorInput();
		
		IFile fmpFile= input.getFile();
		String[] fullpath= fmpFile.getFullPath().segments();
		
		String divaFile= "";
		
		for(int i=1; i<fullpath.length;i++){
			divaFile= divaFile+fullpath[i]+"/";
		}
		
		divaFile=divaFile.substring(0,divaFile.length()-1)+".diva";
		
		IProject project= input.getFile().getProject();
		IFile file= project.getFile(divaFile);
		
		
		TreeSelection fmpselection= (TreeSelection)editor1.getSelection();
		Node selected_element= (Node)fmpselection.getFirstElement();
		String selected_element_name= getName(selected_element);	
		
		
		IFileEditorInput divaInput= new FileEditorInput(file);
		
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(divaInput, "diva.presentation.DivaEditorID");
			
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DivaEditor editor= (DivaEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		editor.setFocus();
		
		boolean found= false;
		
		for(TreeIterator<EObject> it= ((VariabilityModel)editor.getEditingDomain().getResourceSet().getResources().get(0).getContents().get(1)).eAllContents();it.hasNext();){
			EObject object= it.next();
			if(object instanceof NamedElement){
				String name= ((NamedElement)object).getName();
				if(name!=null&&name.equals(selected_element_name)){
					ISelection selection = new StructuredSelection(object);
					editor.setSelection(selection);
					editor.getViewer().setSelection(selection);
					found= true;
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
		try{
		shell = targetEditor.getSite().getShell();
		}catch(NullPointerException e){
			System.out.println("NullPointerException: uk.ulancs.diva.traceartifacts.pop.actions.FMPTraceArtifactsAction.setActiveEditor");
		}
	}

}
