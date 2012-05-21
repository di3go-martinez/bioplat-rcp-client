package edu.unlp.medicine.bioplat.rcp.application.startup;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.r4j.constants.OSDependentConstants;
import edu.unlp.medicine.r4j.environments.RServeConfigurator;

public class StartupRserve implements IStartup {
	// Logger Object
	private static Logger logger = LoggerFactory.getLogger(StartupRserve.class);

	@Override
	public void earlyStartup() {
		new Job("Rserve - Initializing...") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Initializing rserve", IProgressMonitor.UNKNOWN);

				Process process;
				String R_PATH = OSDependentConstants.PATH_TO_R;
				int port = RServeConfigurator.getInstance().getPort();
				try {
					process = Runtime.getRuntime().exec(R_PATH + " --save --restore -q -e library('Rserve');Rserve(port=" + port + ")");
					process.getInputStream();
					// sleep
					Thread.sleep(800);
				} catch (IOException e) {
					logger.error("Failed to run the Rserve on port " + port + " on the path:" + R_PATH);
					e.printStackTrace();
				} catch (Exception e) {
					logger.error("Failed to run the Rserve on port " + port + " on the path:" + R_PATH);
					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}
		}.schedule();
	}
}
