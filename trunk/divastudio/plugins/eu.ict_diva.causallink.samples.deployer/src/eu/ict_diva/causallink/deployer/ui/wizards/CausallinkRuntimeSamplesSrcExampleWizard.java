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



public class CausallinkRuntimeSamplesSrcExampleWizard extends AbstractExampleWizard {
	
	protected Collection<ProjectDescriptor> getProjectDescriptors() {
		
		List<ProjectDescriptor> projects = new ArrayList<ProjectDescriptor>(9);
		
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.samples.deployer", "zips/tutorial.diva.cas.addressDB.zip", "tutorial.diva.cas.addressDB"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.samples.deployer", "zips/tutorial.diva.cas.calendar.zip", "tutorial.diva.cas.calendar"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.samples.deployer", "zips/tutorial.diva.cas.channel.zip", "tutorial.diva.cas.channel"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.samples.deployer", "zips/tutorial.diva.cas.interfaces.zip", "tutorial.diva.cas.interfaces"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.samples.deployer", "zips/tutorial.diva.cas.notifier.zip", "tutorial.diva.cas.notifier"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.samples.deployer", "zips/tutorial.diva.cas.ranker.zip", "tutorial.diva.cas.ranker"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.samples.deployer", "zips/tutorial.diva.cas.telephony.zip", "tutorial.diva.cas.telephony"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.samples.deployer", "zips/tutorial.diva.cas.ui.zip", "tutorial.diva.cas.ui"));
		projects.add(new ProjectDescriptor("eu.ict_diva.causallink.samples.deployer", "zips/tutorial.diva.cas.runner.zip", "tutorial.diva.cas.runner"));
		
		return projects;
	}

	@Override
	protected AbstractUIPlugin getContainerPlugin() {
		return Activator.getDefault();
	}
}