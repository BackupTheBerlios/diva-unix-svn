package diva.weavingui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.kermeta.smartadapters.drools.standalone.SmartAdaptersDrools;

import diva.VariabilityModel;
import diva.Variant;
import fr.irisa.triskell.eclipse.console.EclipseConsole;
import fr.irisa.triskell.eclipse.console.IOConsole;
import fr.irisa.triskell.eclipse.console.messages.ErrorMessage;
import fr.irisa.triskell.eclipse.console.messages.InfoMessage;

public class WeaveVariants implements IObjectActionDelegate, Runnable {

	protected StructuredSelection currentSelection;
    protected ArrayList<Variant> variants = new ArrayList<Variant>();
	
	public WeaveVariants() {
		super();
	}
	
	public void run() {
		System.out.println("Variants size : " + variants.size());
		
		VariabilityModel model = (VariabilityModel)variants.get(0).eResource().getContents().get(0);
		
		
		weaveConfiguration(model, variants, new EclipseConsole("DiVA"), true);
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
			variants.clear();
			currentSelection = (StructuredSelection)selection;
			Iterator it = currentSelection.iterator();

			while(it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof Variant) {
					variants.add((Variant)obj);
				}
			}

		}
	}
	
	public static IFile weaveConfiguration(VariabilityModel model, ArrayList<Variant> aspects, IOConsole console, boolean overwrite) {
		
		// Sort the aspect by priority
		Collections.sort(aspects, new VariantComparator());
		
		if (model.getModel() == null || model.getModel().getUri() == null) {
			console.println(new ErrorMessage("ERROR: The URI for the base model is not set."));
			return null;
		}
			
		
		// Load the base model
		ResourceSet rs = new ResourceSetImpl();
		URI xmiuri = URI.createURI(model.getModel().getUri(), true);
		Resource base_model = rs.getResource(xmiuri, true);
		try {
			base_model.load(null);
		} catch (IOException e) {
			console.println(new ErrorMessage("ERROR: Unable to load base model: " + e));
		}
		EcoreUtil.resolveAll(base_model);
	
		
		if (base_model.getContents().size() == 0 || !base_model.getContents().get(0).eClass().getName().equals("System")) {
			console.println(new ErrorMessage("ERROR: The root of the base model is not an ART system. Please check the base model URI and content."));
			return null;
		}
		
		URI aspect_folder = model.eResource().getURI().trimSegments(1).appendSegment("gen_aspects");
		
		ArrayList<String> variantsURI = new ArrayList<String>();
		
		String ConfigName = "Cfg";
		
		for (Variant v : aspects) {
			 String fname = "";
			if (v.getWeaveLevel() < 0 || v.getWeaveLevel() > 99) {
			v.setWeaveLevel(0);
			}
			if (v.getWeaveLevel() < 10) fname += "0" + v.getWeaveLevel();
			else fname += "" + v.getWeaveLevel();
			fname += "_" + v.getName();
			
			URI aspectURI = aspect_folder.appendSegment(fname + ".drl");
			
			IFile aspect_model_file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(aspectURI.toPlatformString(true)));
			
			
			if ( ! new File(aspect_model_file.getLocation().toOSString()).exists()) {
				console.println(new ErrorMessage("ERROR: No Compiled Aspect found for variant: " + v.getName()));
				continue;
			}
			
			
			variantsURI.add(aspect_model_file.getLocation().toOSString());
			ConfigName += "_" + v.getName();
		}
		
		URI out_uri = model.eResource().getURI().trimSegments(1).appendSegment("gen_configurations").appendSegment(ConfigName + ".art");
		IFile out_file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(out_uri.toPlatformString(true)));
		
		if (out_file.exists() && !overwrite) return out_file;
		
		IFile base_model_file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(xmiuri.toPlatformString(true)));
		
		
		console.println(new InfoMessage("Weaving " + variantsURI.size() + " Aspects." ));
		console.println(new InfoMessage("Base model: " + base_model_file.toString() ));
		console.println(new InfoMessage("Output file: " + out_file.toString() ));
		
		try {
			/*SmartAdaptersDrools.weaveConfiguration("file:/"+base_model_file.getRawLocation().toString(),
			                                       "file:/"+out_file.getRawLocation().toString(),
			                                       variantsURI );
			*/
			String[] params = new String[3];
			params[0] = "file:/"+base_model_file.getRawLocation().toString();
			params[1] = "file:/"+out_file.getRawLocation().toString();
			for(String uri : variantsURI){
				params[2] = uri;
				SmartAdaptersDrools.main(params);				
				console.println(new InfoMessage("  Cleaning..."));
				long start2 = System.currentTimeMillis();
				clean.Main.main(new String[]{params[1]});
				console.println(new InfoMessage("  Done in "+(System.currentTimeMillis()-start2)+" ms"));
				System.gc();
				params[0] = params[1].replace(".art", ".clean.art");
			}
		} catch (Throwable e) {
			console.println(new ErrorMessage("ERROR: An Exception was raised during weaving: " + e.toString()));
		}
		
		console.println(new InfoMessage("Weaving terminated."));
		
		return out_file;
		
	}
	
	public static void openConfiguration(VariabilityModel model,
			ArrayList<Variant> variants, EclipseConsole c) {
		IFile config = WeaveVariants.weaveConfiguration(model, variants, c, false);
		
		IFile cfg = null;
		
		// transform to artext version:
		try {
			ResourceSet rs = new ResourceSetImpl();
			URI xmiuri = URI.createPlatformResourceURI(config.getFullPath().toString(), true);
			Resource xmires = rs.getResource(xmiuri, true);
			xmires.load(null);
			EcoreUtil.resolveAll(xmires);
			
			Resource artres = rs.createResource(xmires.getURI().trimFileExtension().appendFileExtension("artext"));
			artres.getContents().addAll(xmires.getContents());
			artres.save(null);
			
			cfg = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(xmires.getURI().trimFileExtension().appendFileExtension("artext").toPlatformString(true)));
			
		} catch (IOException e) {
			c.println(new ErrorMessage("Error creating artext file: " + e));
		}
		
		final IFile to_open = cfg;
		
		if (to_open != null) {
			
			try {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IEditorInput editorInput = new FileEditorInput(to_open);
						IWorkbenchWindow window=PlatformUI.getWorkbench().getActiveWorkbenchWindow();
						IWorkbenchPage page = window.getActivePage();
						try {
							page.openEditor(editorInput, "art.resource.artext.ui.ArtextEditor");
						} catch (PartInitException e) {
							e.printStackTrace();
						}
					}	
				    });
				
			} catch (Exception e) {
				c.println(new ErrorMessage("Error opening configuration: " + e));
				e.printStackTrace();
			}
		}
	}
}

class VariantComparator implements Comparator<Variant> {

	public int compare(Variant arg0, Variant arg1) {
		return arg0.getWeaveLevel() - arg1.getWeaveLevel();
	}
	
}
