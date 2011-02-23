/* $Id: RunLogo.java,v 1.5 2007-12-06 14:48:47 dvojtise Exp $
 * License   : EPL
 * Copyright : IRISA / INRIA
 * ----------------------------------------------------------------------------
 * Authors   : 
 *		Didier Vojtisek
 */

package eu.ict_diva.causallink.deployer.ui.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import eu.ict_diva.causallink.deployer.Activator;
import fr.irisa.triskell.eclipse.wizard.AbstractExampleWizard;



public class CausallinkRuntimeToolsBinExampleWizard extends AbstractExampleWizard {
	
	protected Collection getProjectDescriptors() {
		
		//System.out.println("Debug - enter in the wizard");
		// We need the statements example to be unzipped along with the
		// EMF library example model, edit and editor examples
		List projects = new ArrayList(1);
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/diva.model.osgi.zip", "diva.model.osgi"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/eu.ict_diva.divastudio.services.zip", "eu.ict_diva.divastudio.services"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/eu.ict_diva.osgi.component.framework.zip", "eu.ict_diva.osgi.component.framework"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/eu.ict_diva.runtime.causallink.osgi.gui.zip", "eu.ict_diva.runtime.causallink.osgi.gui"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/eu.ict_diva.runtime.causallink.osgi.zip", "eu.ict_diva.runtime.causallink.osgi"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/eu.ict_diva.runtime.causallink.zip", "eu.ict_diva.runtime.causallink"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/eu.ict_diva.runtime.checker.zip", "eu.ict_diva.runtime.checker"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/eu.ict_diva.runtime.osgi.bootstrap.zip", "eu.ict_diva.runtime.osgi.bootstrap"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/org.kermeta.osgi.bundleFactory.zip", "org.kermeta.osgi.bundleFactory"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.deployer", "zips/org.kermeta.osgi.instanceFactory.zip", "org.kermeta.osgi.instanceFactory"));
		return projects;
	}

	@Override
	protected AbstractUIPlugin getContainerPlugin() {
		return Activator.getDefault();
	}
}