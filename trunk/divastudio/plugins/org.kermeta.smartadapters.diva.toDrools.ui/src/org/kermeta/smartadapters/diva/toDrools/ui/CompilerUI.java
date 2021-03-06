package org.kermeta.smartadapters.diva.toDrools.ui;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CompilerUI implements IObjectActionDelegate, Runnable {

	/*public static final String DIVA_KERMETA_CODE = "platform:/plugin/org.kermeta.smartadapters.diva.toDrools.ui/src/kermeta/Art2DROOLSPatternFramework.kmt";
	public static final String ENTRY_POINT = "smartadapters4ART::Main";
	 */

	protected StructuredSelection currentSelection;
	protected IFile file;

	public void run() {
		String aspect_uri = "file:/" + file.getLocation().toOSString().replace("\\", "/");
		String target_uri = file.getLocation().toOSString().replace("\\", "/").replace(".smART",".drl");
		process(aspect_uri, target_uri);		
	}


	public static void process(String aspect_uri, String target_file) {
		//IOConsole console = new EclipseConsole("SmartAdapters compiler for DiVA");
		//console.println(new InfoMessage("Compiling DiVA aspect models: " + aspect_uri + "..."));
		
		
		compiler.Main.main(new String[]{aspect_uri});
		//ScalaAspect.smartadapters4ART.RichFactory.createMain().main(aspect_uri);
		
		
		//console.println(new OKMessage("Execution terminated successfully."));		
	}

	/*public static void process(String aspect_uri, String target_file, IOConsole console) {
		console.println(new InfoMessage("Compiling DiVA aspect models: " + aspect_uri + "..."));

		try {			
		    KExecMain.run("process", aspect_uri, target_file, console, DIVA_KERMETA_CODE, ENTRY_POINT);
			console.println(new OKMessage("Execution terminated successfully."));
		} catch (Throwable e) {
			console.println(new ErrorMessage("Runtime error : "));
			console.println(new ThrowableMessage(e));
			e.printStackTrace();
		}		
	}*/

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
			Iterator<StructuredSelection> it = currentSelection.iterator();

			while(it.hasNext()) {
				file = (IFile)it.next();
			}
		}
	}
}
