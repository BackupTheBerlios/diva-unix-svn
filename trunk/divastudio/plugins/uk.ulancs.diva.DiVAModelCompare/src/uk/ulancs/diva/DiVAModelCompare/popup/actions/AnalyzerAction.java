package uk.ulancs.diva.DiVAModelCompare.popup.actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.internal.statistic.NameSimilarity;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;

import uk.ulancs.diva.DiVAModelCompare.Compare.Difference;


import ca.uwaterloo.gp.fmp.ConfigState;
import ca.uwaterloo.gp.fmp.Feature;
import ca.uwaterloo.gp.fmp.FmpFactory;
import ca.uwaterloo.gp.fmp.Node;
import ca.uwaterloo.gp.fmp.Project;
import ca.uwaterloo.gp.fmp.TypedValue;
import ca.uwaterloo.gp.fmp.ValueType;
import ca.uwaterloo.gp.fmp.constraints.ConfiguratorDictionary;
import ca.uwaterloo.gp.fmp.provider.command.RemoveWithPropertiesCommand;
import ca.uwaterloo.gp.fmp.system.ModelManipulation;
import ca.uwaterloo.gp.fmp.system.NodeIdDictionary;
import ca.uwaterloo.gp.fmp.util.FmpExternalLoader;
import ca.uwaterloo.gp.fmp.util.FmpResourceImpl;

import diva.Dimension;
import diva.NamedElement;
import diva.Variant;

public class AnalyzerAction implements IObjectActionDelegate, IViewActionDelegate {

	private Shell shell;
	private Vector<IFile> Trees;
	private List<Difference> changes= new ArrayList<Difference>();

	
	
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
	

	public void getDifferences(List<DiffElement> differences){
		for(Iterator<DiffElement> it= differences.iterator();it.hasNext();){
			DiffElement diffElement= it.next();
			getDifferences(new ArrayList<DiffElement>(diffElement.getSubDiffElements()));
			System.out.println(diffElement);
		}
	}
	
	public void doReplace(String fname, String oldPattern, String replPattern){
		String line;
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(fname);
			BufferedReader reader=new BufferedReader ( new InputStreamReader(fis));
			while((line = reader.readLine()) != null) {
				line = line.replaceAll(oldPattern, replPattern);
				sb.append(line+"\n");
			}
			reader.close();
			BufferedWriter out=new BufferedWriter ( new FileWriter(fname));
			out.write(sb.toString());
			out.close();
		}
		catch (Throwable e) {
		            System.err.println("*** exception ***");
		}
	}
	
	public List<Feature> removeFeature(Node model,FmpExternalLoader Tree, String feature_name){
		Object[] children= model.getChildren().toArray();
		List<Feature> features= new ArrayList<Feature>();
		for(int i=0; i<children.length;i++){
			Node node= (Node)children[i];
			if(node instanceof Feature){
			Feature feature= (Feature)children[i];
			String name= getName(feature);
			
			
			if(name.equals(feature_name)){
				model.getChildren().remove(feature);	
				features.add(feature);
			}else{
				 features.addAll(removeFeature(node,Tree,feature_name));
			}
			}else{
				 features.addAll(removeFeature(node,Tree,feature_name));
			}
		}
		return features;
		
	}
	
	public void renameFeature(Node model,FmpExternalLoader Tree, String original_feature_name, String new_feature_name){
		Object[] children= model.getChildren().toArray();
		for(int i=0; i<children.length;i++){
			Node node= (Node)children[i];
			if(node instanceof Feature){
			Feature feature= (Feature)node;
			String name= getName(feature);		
			
			
			if(name.equals(original_feature_name)){
				//model.getChildren().remove(feature);
				feature.setName(new_feature_name);
				feature.setId(new_feature_name);
				feature.getProperties().setName(new_feature_name);
				feature.getProperties().setId(new_feature_name);
				//model.getChildren().add(feature);
				
				return;
			}else{
				renameFeature(node,Tree,original_feature_name,new_feature_name);
			}
			}else{
				renameFeature(node,Tree,original_feature_name,new_feature_name);
			}
		}
	}
	
	public void addDimension(Feature model,FmpExternalLoader Tree, Project project,Dimension dimension){
		Feature parent_feature= createFeature(dimension.getName(),"core",project);	
		Feature new_model_parent_feature = FmpFactory.eINSTANCE.createFeature();
		new_model_parent_feature.setProperties(parent_feature.getProperties());
		model.getChildren().add(new_model_parent_feature);
		
		for(Iterator<Variant> it= dimension.getVariant().iterator(); it.hasNext();){
			Variant variant= it.next();
			addVariant(model,Tree,project,variant);
		}
		
	}
	
	public void addVariant(Node model,FmpExternalLoader Tree,Project project, Variant variant){
		Object[] children= model.getChildren().toArray();
		for(int i=0; i<children.length;i++){
			Node node= (Node)children[i];
			if(node instanceof Feature){
			Feature feature= (Feature)node;
			String name= getName(feature);	
			if(name.equals(variant.getType().getName())){
				//model.getChildren().remove(feature);
				//ModelManipulation.INSTANCE.remove(feature, Tree.getEditingDomain(), null, true);
				Feature new_feature= createFeature(variant.getName(),"core",project);	
				Feature new_model_feature = FmpFactory.eINSTANCE.createFeature();
				new_model_feature.setProperties(new_feature.getProperties());
				feature.getChildren().add(new_model_feature);
				//model.getChildren().add(feature);
				return;
			}else{
				addVariant(node,Tree,project,variant);
			}
			}else{
				addVariant(node,Tree,project,variant);
			}
		}
	}
	
	public Feature createFeature(String feature_name, String feature_type,Project project){
		Feature metamodel= project.getMetaModel();
		
		Feature metaFeature = (Feature) metamodel.getChildren().get(0);
		Feature new_feature = FmpFactory.eINSTANCE.createFeature();

		new_feature.setProperties(ModelManipulation.INSTANCE.configure(metaFeature));
	
		
		ca.uwaterloo.gp.fmp.system.MetaModel mm = new ca.uwaterloo.gp.fmp.system.MetaModel();

		if ( feature_type == null || feature_type.equals("core")){
			/*Metamodel. */mm.setFeatureAttributes(new_feature, feature_name, feature_name, ConfigState.UNDECIDED_LITERAL, ValueType.NONE_LITERAL, null, null, 0, 1);

		}
		else//caso contrário é opcional
		{
			/*Metamodel. */mm.setFeatureAttributes(new_feature, feature_name, feature_name, ConfigState.UNDECIDED_LITERAL, ValueType.NONE_LITERAL, null, null, 0, 1);
		}
		metaFeature.getConfigurations().add(new_feature);
		if(new_feature.getTypedValue()!=null){
			String temp =new_feature.getTypedValue().getStringValue();
		}
		return new_feature;
	}
	
	public String getName(Node n){
		Feature properties= n.getProperties();
		
		if(properties!=null){
		Object property= ((Feature)properties.getChildren().get(0)).eAdapters().get(0).getTarget();
			if(property instanceof TypedValue){
				return ((TypedValue)property).getStringValue();
			}
		}
		return null;
	}
	
	public IFile getFMP(){
		ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(shell, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);

		dialog.setTitle("FMP Selection");

		dialog.open();
		
		if(dialog.getReturnCode()==Window.OK){
			return (IFile)dialog.getResult()[0];
			
		}
		return null;
	}
	
	public List<DiffElement> getDiVADifferences(IFile leftModel, IFile rightModel){
		
		final ResourceSet resourceSet1 = new ResourceSetImpl();
		final ResourceSet resourceSet2 = new ResourceSetImpl();

		try {
			System.out.println("Loading resources.\n");
			
			IFile file1;
			IFile file2;

			if(leftModel.getLocalTimeStamp()>rightModel.getLocalTimeStamp()){
				file1=Trees.get(1);
				file2=Trees.get(0);
			}else{
				file1=Trees.get(0);
				file2=Trees.get(1);
			}
			
			
			System.out.println("timestamp: "+file1.getName()+ " "+file1.getLocalTimeStamp()+" "+file2.getName()+ " "+file2.getLocalTimeStamp());
			
			try {
				IContentType contentType = file1.getContentDescription().getContentType();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final EObject model1 = EclipseModelUtils.load(file1, resourceSet1);
			final EObject model2 = EclipseModelUtils.load(file2, resourceSet2);

			// Creates the match then the diff model for those two models
			System.out.println("Matching models.\n"); //$NON-NLS-1$
			final MatchModel match = MatchService.doMatch(model2, model1, Collections
					.<String, Object> emptyMap());
			System.out.println("Differencing models.\n"); //$NON-NLS-1$
			final DiffModel diff = DiffService.doDiff(match, false);
			
			final Iterator<EObject> it = diff.eAllContents();
			
			while (it.hasNext()) {
				final DiffElement element = (DiffElement)it.next();
				
				if (element instanceof ModelElementChangeLeftTarget){
					ModelElementChangeLeftTarget left= (ModelElementChangeLeftTarget)element;
					
					if(left.getLeftElement() instanceof NamedElement){
					NamedElement changedElement=(NamedElement)left.getLeftElement();
					
					System.out.println(element.getKind());
					String kind= element.getKind().getName();
					
					if(kind.equals(Difference.DELETION)){
						Difference difference= new Difference(changedElement,null,Difference.DELETION);
						changes.add(difference);
						
					}else if(kind.equals(Difference.ADDITION)){
						Difference difference= new Difference(null,changedElement,Difference.ADDITION);
						changes.add(difference);
					}
					}
					
					
				}else if (element instanceof ModelElementChangeRightTarget){
					ModelElementChangeRightTarget right= (ModelElementChangeRightTarget)element;
					
					if(right.getRightElement() instanceof NamedElement){
					NamedElement changedElement=(NamedElement)right.getRightElement();
				
					System.out.println(element.getKind());
					
					String kind= element.getKind().getName();
					
					if(kind.equals(Difference.DELETION)){
						Difference difference= new Difference(changedElement,null,Difference.DELETION);
						changes.add(difference);
						
					}else if(kind.equals(Difference.ADDITION)){
						Difference difference= new Difference(null,changedElement,Difference.ADDITION);
						changes.add(difference);
					}
					}
					
					
				}else if(element instanceof UpdateAttribute){
					UpdateAttribute change= (UpdateAttribute)element;
					
					Difference difference= new Difference((NamedElement)change.getRightElement(),(NamedElement)change.getLeftElement(),Difference.CHANGE);
					changes.add(difference);

					System.out.println(change.getKind());
					
				}
				
			}
			

			System.out.println("Merging difference to args[1].\n"); //$NON-NLS-1$
			final List<DiffElement> differences = new ArrayList<DiffElement>(diff.getOwnedElements());
			
			getDifferences(differences);
			return differences;
		} catch (final IOException e) {
			// shouldn't be thrown
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
			return null;
	}
	
	public void applyDifferences(IFile TreeFile){
		FmpExternalLoader Tree = new FmpExternalLoader();
		Tree.load(TreeFile);
		
		FmpResourceImpl fmpresource= (FmpResourceImpl)Tree.getResources().get(0);

		Project project= (Project) fmpresource.getAllContents().next();
		Feature model= project.getModel();	
		
		
		
		for(Iterator<Difference> it= changes.iterator();it.hasNext();){
			Difference difference= it.next();
			if(difference.getType().equals(Difference.ADDITION)){
				if(difference.getAfter() instanceof Variant){
					addVariant(model,Tree,project,(Variant)difference.getAfter());
				}else if(difference.getAfter() instanceof Dimension){
					addDimension(model,Tree,project,(Dimension)difference.getAfter());
				}
				
			}else if(difference.getType().equals(Difference.DELETION)){
				
				List<Feature> features= removeFeature(model,Tree,difference.getBefore().getName());
				for(Iterator<Feature> feature_it= features.iterator();feature_it.hasNext();){
				Feature feature= feature_it.next();
				//ConfiguratorDictionary.INSTANCE.remove(feature);
			
				
				ModelManipulation.INSTANCE.remove(feature, Tree.getEditingDomain(), null, false);
				}
				
			}else if(difference.getType().equals(Difference.CHANGE)){
				renameFeature(model,Tree,difference.getBefore().getName(),difference.getAfter().getName());
			}
			
		}
		
		Map options = new HashMap();
		options.put(XMLResource.OPTION_ENCODING, "ASCII");
		//System.out.println("URI : " + resource.g);
		//resource.setURI(URI.create(scheme, opaquePart, fragment)("http:///fmp.ecore"));
		try {
			fmpresource.save(options);
			changes= new ArrayList<Difference>();
			
			//this.doReplace(new File("/Users/greenwop/runtime-EclipseApplication/Test/"+TreeFile.getName()).getAbsolutePath(), "http:///fmp2.ecore", "http:///fmp.ecore");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		changes= new ArrayList<Difference>();
		List<DiffElement> differences= getDiVADifferences(Trees.get(0),Trees.get(1));
		applyDifferences(getFMP());
		
		
			MessageDialog.openInformation(
					shell,
					"DiVAModelCompare",
				"DiVA Model Changes Applied.");
			
		
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		try{
		Trees.removeAllElements();
		Iterator<IFile> it= ((StructuredSelection) selection).iterator();
		
		while(it.hasNext()){
			IFile file= it.next();
			Trees.add(file);
		}
		}catch(Exception e){
			
		}
		
	}

	public void init(IViewPart view) {
		// TODO Auto-generated method stub
		
	}

}
