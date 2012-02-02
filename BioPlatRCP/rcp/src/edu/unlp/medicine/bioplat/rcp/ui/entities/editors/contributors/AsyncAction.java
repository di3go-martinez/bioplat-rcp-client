package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class AsyncAction {

	public static IAction wrap(final IAction targetAction) {
		IAction proxyAction = new Action(targetAction.getText(), targetAction.getImageDescriptor()) {
			@Override
			public void run() {
				Job j = new Job(getText()) {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							// Cambiar el IAction y delegar el uso del monitor,
							// para así sacar el UNKNOWN
							monitor.beginTask("Procesando...", IProgressMonitor.UNKNOWN);
							targetAction.run();
						} finally {
							monitor.done();
						}
						return Status.OK_STATUS;
					}
				};
				j.setUser(true);
				j.setPriority(Job.LONG);
				j.schedule();
			}
		};
		return proxyAction;
	}
}
