package edu.unlp.medicine.bioplat.rcp.application.startup;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.r4j.constants.OSDependentConstants;
import edu.unlp.medicine.r4j.environments.RServeConfigurator;

public class StartupRserve implements IStartup {
	// Logger Object
	private static Logger logger = LoggerFactory.getLogger(StartupRserve.class);

	private static final String R_PATH = OSDependentConstants.PATH_TO_R;
	private Process process;

	@Override
	public void earlyStartup() {

		new Job("Rserve - Initializing...") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Initializing rserve", IProgressMonitor.UNKNOWN);

				int port = RServeConfigurator.getInstance().getPort();
				try {
					process = Runtime.getRuntime().exec(R_PATH + " --save --restore -q -e library('Rserve');Rserve(port=" + port + ")");
					process.getInputStream();
					// sleep
					Thread.sleep(800);
				} catch (Exception e) {
					final String msg = "Failed to run the Rserve on port " + port + " on the path:" + R_PATH;
					logger.error(msg, e);
					e.printStackTrace();
					MessageManager.INSTANCE.add(Message.error(msg, e));
				}

				return Status.OK_STATUS;
			}
		}.schedule();

		// Agrego un listener para cuado se baje la aplicación se ejecute
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				// FIXME implementar el shutdown del Rserve o un kill
				// if (process != null) {
				// process.destroy();
				// process = null;
				// }

			}

		});
	}
}
