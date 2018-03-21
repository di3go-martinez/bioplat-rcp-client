package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;

public class AsyncAction {

	public static IAction wrap(final IAction targetAction) {
		IAction proxyAction = new Action(targetAction.getText(), targetAction.getImageDescriptor()) {
			@Override
			public void run() {
				Job j = new Job(getText()) {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							// TODO Cambiar el IAction y delegar el uso del
							// monitor, para así sacar el UNKNOWN
							monitor.beginTask("Processing Request...", IProgressMonitor.UNKNOWN);
							targetAction.run();
						} catch (Exception e) {
							MessageManager.INSTANCE.add(Message.error("Unexpected Exception running the action \"" + targetAction.getText() + "\"", e));
							return ValidationStatus.error(e.getMessage());
						} finally {
							monitor.done();
						}
						return ValidationStatus.ok();
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
