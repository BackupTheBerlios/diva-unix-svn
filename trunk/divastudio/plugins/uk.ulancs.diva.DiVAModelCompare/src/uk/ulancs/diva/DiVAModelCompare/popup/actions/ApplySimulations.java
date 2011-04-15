package uk.ulancs.diva.DiVAModelCompare.popup.actions;




import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;


import ca.uwaterloo.gp.fmp.Feature;
import ca.uwaterloo.gp.fmp.Node;
import ca.uwaterloo.gp.fmp.Project;
import ca.uwaterloo.gp.fmp.TypedValue;
import ca.uwaterloo.gp.fmp.impl.FeatureGroupImpl;
import ca.uwaterloo.gp.fmp.system.NodeIdDictionary;
import ca.uwaterloo.gp.fmp.util.FmpExternalLoader;
import ca.uwaterloo.gp.fmp.util.FmpResourceImpl;

import diva.ConfigVariant;
import diva.Configuration;
import diva.Context;
import diva.NamedElement;
import diva.Scenario;
import diva.SimulationModel;
import diva.VariabilityModel;
import diva.provider.DiVA_visitorEditPlugin;

public class ApplySimulations implements IObjectActionDelegate, IViewActionDelegate {
	
	private Shell shell=null;
	private IFile file;
	public VariabilityModel diva;

	
	public void setActivePart(IAction arg0, IWorkbenchPart targetPart) {
			shell = targetPart.getSite().getShell();
	}
	
	
	
	public Feature getFeatureModel(){
		ResourceListSelectionDialog fmpdialog = new ResourceListSelectionDialog(shell, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);

		fmpdialog.setTitle("FMP Selection");

		fmpdialog.open();
		
		if(fmpdialog.getReturnCode()==Window.OK){
			IFile fmp= (IFile)fmpdialog.getResult()[0];
			 
			FmpExternalLoader Tree = new FmpExternalLoader();
			Tree.load(fmp);
				
			FmpResourceImpl fmpresource= (FmpResourceImpl)Tree.getResources().get(0);

			Project project= (Project) fmpresource.getAllContents().next();
			return project.getModel();	
			
			
		}
		return null;
	}
	
	public Configuration getConfiguration(){
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
			    shell,
			    new FileTreeLabelProvider(),
			    new FileTreeContentProvider());
			dialog.setTitle("Configuration Selection");
			dialog.setMessage("Select a configuration from the tree:");

		    dialog.setInput(System.getProperty("user.home"));

			dialog.open();
			
			
		if(dialog.getReturnCode()==Window.OK){
			return (Configuration)dialog.getResult()[0];
	
		}
		return null;
	}
	
	public List<String> getRequirementsText(EList l,String owner,Node root,Configuration config){
		List<String> requirements= new ArrayList<String>();

		for(Iterator it= l.iterator();it.hasNext();){
			Object child= it.next();
			
			if(child instanceof Node){
				Node node= (Node)child;
				
				if(node!=null){
					String nodeName = getName(node);
					
					if (nodeName!=null) {
						
						if (nodeName.contains("group")) {
							requirements.addAll(getRequirementsText(node.getChildren(),nodeName,root, config));
						}
						else {
							for(Iterator<ConfigVariant> it2= config.getVariant().iterator();it2.hasNext();){
								ConfigVariant variant= it2.next();
								if(nodeName.equalsIgnoreCase(variant.getVariant().getName())){
									String description= getDescription(node);
									if(!requirements.contains(description.trim()))
										requirements.add(getDescription(node).trim());
								}
							}
							if (node.getChildren().size() > 0) {
								for(String description : getRequirementsText(node.getChildren(),nodeName,root, config)){
									if(!requirements.contains(description.trim()))
										requirements.add(description.trim());
								}								
							}
						}
					}
				}
			}
		}
		
		return requirements;
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
  	
  	public String getDescription(Node n){
		Feature properties= n.getProperties();
		
		if(properties!=null){
			Object property = ((Feature)properties.getChildren().get(3)).getChildren().get(0);
			if (property instanceof FeatureGroupImpl) {
				Feature First = (Feature)((FeatureGroupImpl)property).getChildren().get(2);
				Object Second = ((Feature)First.getChildren().get(0)).eAdapters().get(0).getTarget();
							
				if(Second instanceof TypedValue){
					return ((TypedValue)Second).getStringValue();
				}
			}
		}
		return null;
	}
  	
  	
  	public void saveRequirements(List<String> requirements){
  		
  		FileDialog dialog = new FileDialog(new Shell(Display.getCurrent()), SWT.SAVE);
  		
  		String filename=dialog.open();
    	
  
  		
  		if(filename!=null){
  			
  			if(!filename.endsWith(".txt")){
  				filename=filename+".txt";
  			}
  			
  			
			try {
				FileWriter writer = new FileWriter(filename);
				
				BufferedWriter buff= new BufferedWriter(writer);
	  			
	  			for(Iterator<String> it= requirements.iterator();it.hasNext();){
	  				
	  				String req= it.next();
	  				
	  				if(it.hasNext())
	  					req=req+"\n\n";
	  				buff.write(req);
	  				buff.flush();
	  			}
	  			
	  			buff.close();
	  			writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  			
  		}
  	}

	public void run(IAction arg0) {
		final ResourceSet resourceSet1 = new ResourceSetImpl();
		
		try {
			diva = (VariabilityModel)EclipseModelUtils.load(file, resourceSet1);
			
			Configuration config= getConfiguration();
			
			if(config!=null){
			
				Feature model= getFeatureModel();
				if(model!=null){
					NodeIdDictionary.INSTANCE.visit(null, model);
					
					List<String> requirements= getRequirementsText(model.getChildren(),model.getName(),model,config);
					
					saveRequirements(requirements);
				}
				file.getProject().refreshLocal(IResource.DEPTH_INFINITE,null);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	


	public void selectionChanged(IAction arg0, ISelection selection) {
		try{
		file= (IFile) (((StructuredSelection) selection)).getFirstElement();
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	class FileTreeContentProvider implements ITreeContentProvider {
		  /**
		   * Gets the children of the specified object
		   * 
		   * @param arg0
		   *            the parent object
		   * @return Object[]
		   */
		  public Object[] getChildren(Object arg0) {
		    // Return the files and subdirectories in this directory
		    if(arg0 instanceof SimulationModel)
		    	return ((SimulationModel)arg0).getScenario().toArray();
		    else if(arg0 instanceof Scenario)
		    	return ((Scenario)arg0).getContext().toArray();
		    else if(arg0 instanceof Context){
		    	List configurations= new ArrayList<Configuration>();
		    	configurations.addAll(((Context)arg0).getConfiguration());
		    	Collections.sort(configurations, new Comparator<Configuration>(){
					public int compare(Configuration o1,Configuration o2){
						return o2.getTotalScore()-o1.getTotalScore();
					}
				});
		    	
		    	return configurations.toArray();
		    }
		    
		    return null;
		  }

		  /**
		   * Gets the parent of the specified object
		   * 
		   * @param arg0
		   *            the object
		   * @return Object
		   */
		  public Object getParent(Object arg0) {
			 return null;
		    
		  }

		  /**
		   * Returns whether the passed object has children
		   * 
		   * @param arg0
		   *            the parent object
		   * @return boolean
		   */
		  public boolean hasChildren(Object arg0) {
		    // Get the children
		    Object[] obj = getChildren(arg0);

		    // Return whether the parent has children
		    return obj == null ? false : obj.length > 0;
		  }

		  /**
		   * Gets the root element(s) of the tree
		   * 
		   * @param arg0
		   *            the input data
		   * @return Object[]
		   */
		  public Object[] getElements(Object arg0) {
		    // These are the root elements of the tree
		    // We don't care what arg0 is, because we just want all
		    // the root nodes in the file system
			  return diva.getSimulation().getScenario().toArray();
		  }

		  /**
		   * Disposes any created resources
		   */
		  public void dispose() {
		    // Nothing to dispose
		  }

		  /**
		   * Called when the input changes
		   * 
		   * @param arg0
		   *            the viewer
		   * @param arg1
		   *            the old input
		   * @param arg2
		   *            the new input
		   */
		  public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		    // Nothing to change
		  }


		}

		/**
		 * This class provides the labels for the file tree
		 */

		class FileTreeLabelProvider implements ILabelProvider {
		  // The listeners
		  private List listeners;

		  // Images for tree nodes
		  private Image context, scenario, configuration,simulation;

		  // Label provider state: preserve case of file names/directories
		  boolean preserveCase;

		  /**
		   * Constructs a FileTreeLabelProvider
		   */
		  public FileTreeLabelProvider() {
		    // Create the list to hold the listeners
		    listeners = new ArrayList();


		    context= ImageDescriptor.createFromURL((URL) DiVA_visitorEditPlugin.INSTANCE.getImage("full/obj16/Context")).createImage();
		    scenario= ImageDescriptor.createFromURL((URL) DiVA_visitorEditPlugin.INSTANCE.getImage("full/obj16/Scenario")).createImage();

		    configuration= ImageDescriptor.createFromURL((URL) DiVA_visitorEditPlugin.INSTANCE.getImage("full/obj16/Configuration")).createImage();
		    simulation= ImageDescriptor.createFromURL((URL) DiVA_visitorEditPlugin.INSTANCE.getImage("full/obj16/SimulationModel")).createImage();
		  }

		  /**
		   * Sets the preserve case attribute
		   * 
		   * @param preserveCase
		   *            the preserve case attribute
		   */
		  public void setPreserveCase(boolean preserveCase) {
		    this.preserveCase = preserveCase;

		    // Since this attribute affects how the labels are computed,
		    // notify all the listeners of the change.
		    LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
		    for (int i = 0, n = listeners.size(); i < n; i++) {
		      ILabelProviderListener ilpl = (ILabelProviderListener) listeners
		          .get(i);
		      ilpl.labelProviderChanged(event);
		    }
		  }

		  /**
		   * Gets the image to display for a node in the tree
		   * 
		   * @param arg0
		   *            the node
		   * @return Image
		   */
		  public Image getImage(Object arg0) {
			  if(arg0 instanceof SimulationModel)
			    	return simulation;
			    else if(arg0 instanceof Scenario)
			    	return scenario;
			    else if(arg0 instanceof Context)
			    	return context;
			    else if(arg0 instanceof Configuration)
			    	return configuration;
			  
			  return null;
			 
		  }

		  /**
		   * Gets the text to display for a node in the tree
		   * 
		   * @param arg0
		   *            the node
		   * @return String
		   */
		  public String getText(Object arg0) {
		    if(arg0 instanceof NamedElement)
		    	return ((NamedElement)arg0).getName();
		    else if(arg0 instanceof Configuration)
		    	return "Configuration ("+((Configuration)arg0).getTotalScore()+")";
		    
		    return "";
		  }

		  /**
		   * Adds a listener to this label provider
		   * 
		   * @param arg0
		   *            the listener
		   */
		  public void addListener(ILabelProviderListener arg0) {
		    listeners.add(arg0);
		  }

		  /**
		   * Called when this LabelProvider is being disposed
		   */
		  public void dispose() {
		    // Dispose the images
		    if (context != null)
		      context.dispose();
		    if (simulation != null)
		      simulation.dispose();
		    
		    if(configuration!=null)
		    	configuration.dispose();
		    
		    if(scenario!=null)
		    	scenario.dispose();
		  }

		  /**
		   * Returns whether changes to the specified property on the specified
		   * element would affect the label for the element
		   * 
		   * @param arg0
		   *            the element
		   * @param arg1
		   *            the property
		   * @return boolean
		   */
		  public boolean isLabelProperty(Object arg0, String arg1) {
		    return false;
		  }

		  /**
		   * Removes the listener
		   * 
		   * @param arg0
		   *            the listener to remove
		   */
		  public void removeListener(ILabelProviderListener arg0) {
		    listeners.remove(arg0);
		  }
		}

		public void init(IViewPart view) {
			// TODO Auto-generated method stub
			
		}

}
