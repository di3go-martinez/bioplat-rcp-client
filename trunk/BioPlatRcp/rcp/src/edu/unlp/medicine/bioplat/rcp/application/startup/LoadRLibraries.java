package edu.unlp.medicine.bioplat.rcp.application.startup;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.r4j.environments.R4JSession;
import edu.unlp.medicine.r4j.exceptions.R4JConnectionException;

public class LoadRLibraries implements IStartup {
	// Logger Object
	private static Logger logger = LoggerFactory.getLogger(LoadRLibraries.class);

	@Override
	public void earlyStartup() {

		new Job("Load R Libraries...") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Initializing load R libraries", IProgressMonitor.UNKNOWN);
				try {
					Thread.sleep(800);
					this.validateRServe();
				} catch (R4JConnectionException e) {
					final String msg = "You must install the Rserve package. You must open R and run the following statement: install.packages (Rserve).";
					logger.error(msg, e);
					e.printStackTrace();
					MessageManager.INSTANCE.add(Message.error(msg, e));
					return ValidationStatus.error(msg, e);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}

			/**
			 * This method validates if the server is running. Otherwise, it
			 * raises an exception.
			 * 
			 * @throws R4JConnectionException
			 */
			private void validateRServe() throws R4JConnectionException {
				R4JSession session = new R4JSession("test");
				session.open();
				session.close();
			}

		}.schedule();

	}
}
