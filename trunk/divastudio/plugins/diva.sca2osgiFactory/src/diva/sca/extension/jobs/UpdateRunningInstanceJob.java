package diva.sca.extension.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

public class UpdateRunningInstanceJob extends Job {
	
	public UpdateRunningInstanceJob(String name){
		super(name);
		setUser(true);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

}
