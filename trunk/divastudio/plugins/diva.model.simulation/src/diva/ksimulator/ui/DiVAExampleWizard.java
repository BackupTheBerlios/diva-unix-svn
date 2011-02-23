package diva.ksimulator.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import fr.irisa.triskell.eclipse.plugin.Activator;
import fr.irisa.triskell.eclipse.wizard.AbstractExampleWizard;

public class DiVAExampleWizard extends AbstractExampleWizard {
	
	protected Collection getProjectDescriptors() {
		
		List projects = new ArrayList(1);
		projects.add(new ProjectDescriptor("diva.model", "zips/diva.studies.zip", "diva.studies"));
		return projects;
	}

	@Override
	protected AbstractUIPlugin getContainerPlugin() {
		return Activator.getDefault();
	}
}