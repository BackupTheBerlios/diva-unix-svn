/* $Id: RunLogo.java,v 1.5 2007-12-06 14:48:47 dvojtise Exp $
 * License   : EPL
 * Copyright : IRISA / INRIA
 * ----------------------------------------------------------------------------
 * Authors   : 
 *		Didier Vojtisek
 */

package eu.ict_diva.studies.ui.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import eu.ict_diva.studies.ui.Activator;
import fr.irisa.triskell.eclipse.wizard.AbstractExampleWizard;



public class DivaCASStudyExampleWizard extends AbstractExampleWizard {
	
	protected Collection getProjectDescriptors() {
		
		//System.out.println("Debug - enter in the wizard");
		// We need the statements example to be unzipped along with the
		// EMF library example model, edit and editor examples
		List projects = new ArrayList(1);
		projects.add(new ProjectDescriptor("eu.ict_diva.studies.ui", "zips/eu.ict_diva.study.CAS.zip", "eu.ict_diva.study.CAS"));
		return projects;
	}

	@Override
	protected AbstractUIPlugin getContainerPlugin() {
		return Activator.getDefault();
	}
}