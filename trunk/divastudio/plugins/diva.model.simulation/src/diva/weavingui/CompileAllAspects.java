package diva.weavingui;

import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
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
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.kermeta.smartadapters.diva.toDrools.ui.CompilerUI;

import diva.Dimension;
import diva.VariabilityModel;
import diva.Variant;
import fr.irisa.triskell.eclipse.console.EclipseConsole;
import fr.irisa.triskell.eclipse.console.IOConsole;
import fr.irisa.triskell.eclipse.console.messages.ErrorMessage;
import fr.irisa.triskell.eclipse.console.messages.InfoMessage;
import fr.irisa.triskell.eclipse.console.messages.ThrowableMessage;

public class CompileAllAspects implements IObjectActionDelegate, Runnable {

	protected StructuredSelection currentSelection;
    protected IFile file;
	
	public CompileAllAspects() {
		super();
	}
	
	public void run() {
		
		IOConsole console = new EclipseConsole("DiVA");
		console.println(new InfoMessage("Compiling aspects for diva model : " + file.getLocation().toOSString() + "..."));
		
		
		try {
			
			IContainer diva_folder = file.getParent();
			IResource aspect_gen_folder = diva_folder.findMember("gen_aspects", true);
			if (aspect_gen_folder != null && aspect_gen_folder.exists()) aspect_gen_folder.delete(true, null);
			diva_folder.getFolder(Path.fromPortableString("gen_aspects")).create(true, false, null);
			
			// Load the DiVA model
			ResourceSet rs = new ResourceSetImpl();
			URI diva_uri = URI.createURI("file:/" + file.getLocation().toOSString(), false);
			Resource diva_model = rs.getResource(diva_uri, true);
			diva_model.load(null);
			EcoreUtil.resolveAll(diva_model);
			
			VariabilityModel model = (VariabilityModel)diva_model.getContents().get(0);
			
			for (Dimension d : model.getDimension()) {
				for (Variant v : d.getVariant()) {
					compileVariantAspect(v, console);
				}
			}
			
			
			
		} catch (Throwable e) {
			console.println(new ErrorMessage("Runtime error : "));
			console.println(new ThrowableMessage(e));
			e.printStackTrace();
		}
	}

	public static void compileVariantAspect(Variant variant, IOConsole console) {
		
		URI diva_uri = variant.eResource().getURI();
		// Take care of Windows paths
		diva_uri = URI.createURI(URI.decode(diva_uri.toString()).replace("\\","/"));
		if (diva_uri.isPlatform()) {
			IFile diva_model = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(variant.eResource().getURI().toPlatformString(true)));
			diva_uri = URI.createFileURI(diva_model.getLocation().toString());
		}
		
		console.println(new InfoMessage("Processing variants : " + variant.getName() + " (" + variant.getId() + ")..."));
		if (variant.getId() == null || variant.getId().trim().equals("")) {
			console.println(new ErrorMessage("  SKIPED : Variant has an invalid ID"));
			return;
		}
		if (variant.getModel() == null || variant.getModel().getUri() == null || variant.getModel().getUri().trim().equals("")) {
			console.println(new ErrorMessage("  SKIPED : No aspect model associated to this variant"));
			return;
		}
		String aspectURI = variant.getModel().getUri();
		console.println(new InfoMessage("  URI of the associated aspect is " + aspectURI));

		String fname = "";
		if (variant.getWeaveLevel() < 0 || variant.getWeaveLevel() > 99) {
			variant.setWeaveLevel(0);
		}
		if (variant.getWeaveLevel() < 10) fname += "0" + variant.getWeaveLevel();
		else fname += "" + variant.getWeaveLevel();
		fname += "_" + variant.getName();
		
		URI art_uri = diva_uri.trimSegments(1).appendSegment("gen_aspects").appendSegment(fname + ".smART");
		
		try {
			ResourceSet rset = new ResourceSetImpl();
			URI xmi_uri = URI.createURI(aspectURI);
			Resource artres = rset.getResource(xmi_uri, true);
			artres.load(null);
			EcoreUtil.resolveAll(artres);
			
			console.println(new InfoMessage("  Creating smART model : " + art_uri));
			Resource xmires = rset.createResource(art_uri);
			xmires.getContents().addAll(artres.getContents());
			xmires.save(null);
			
		} catch (Exception e) {
			console.println(new ErrorMessage("  ERROR: Cannot create smART model : " + e.toString()));
			e.printStackTrace();
		}
		console.println(new InfoMessage("  Compiling smART model : " + art_uri));
		CompilerUI.process(art_uri.toString(), art_uri.trimFileExtension().appendFileExtension("drl").toFileString());
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
			currentSelection = (StructuredSelection)selection;
			Iterator it = currentSelection.iterator();

			while(it.hasNext()) {
				file = (IFile)it.next();
			}

		}
		
	}

}
