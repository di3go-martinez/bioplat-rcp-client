package edu.unlp.medicine.bioplat.rcp.application.startup;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;

import edu.unlp.medicine.domainLogic.framework.MetaPlat;

public class LoadBasicData implements IStartup {

	@Override
	public void earlyStartup() {
		Job j = new Job("Initializing...") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Initializing basic data", IProgressMonitor.UNKNOWN);
				MetaPlat.getInstance().getGeneById("1");
				return Status.OK_STATUS;
			}
		};
		j.setUser(true);
		j.schedule();
	}

}
