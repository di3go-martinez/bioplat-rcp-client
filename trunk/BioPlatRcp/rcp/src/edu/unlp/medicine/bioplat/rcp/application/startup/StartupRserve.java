package edu.unlp.medicine.bioplat.rcp.application.startup;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
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

			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Initializing rServe(bridge between Bioplat and R)", IProgressMonitor.UNKNOWN);

				int port = RServeConfigurator.getInstance().getPort();
				final String rcmd = R_PATH + " --save --restore -q -e library('Rserve');Rserve(port=" + port + ")";
				try {
					
					logger.debug("Starting RServe (bridge between Bioplat and R). R Script: " + rcmd);
					process = Runtime.getRuntime().exec(rcmd);
					process.getInputStream();
					// sleep
					Thread.sleep(5000);
				} catch (Exception e) {
					final String msg1 = "Failed to start the Rserve (bridge between Bioplat and R) on port " + port + ". You can use Bioplat but it will not be able to run any R statistics";
					final String msg2 = "Failed to start the Rserve on port " + port + ". You can use Bioplat but it will not be able to run any R statistics. See details on message view.";
					final String details = "4 possible reasons: 1) you don't have permission to open the free port " + port + " for starting RServe on this machine. Ask the system admin. 2) the operating system asked you to start RServe.exe (for connecting Bioplat and R) and you said no. In this case, restart Bioplat and accept to establish the connection with R. 3) you don't have R installed on this folder: " + R_PATH + " 4) the R doesnt have the RServe package installed (it should not happen if you are using the R coming on Bioplat distribution). In this last case check if you have " + R_PATH + "\\library\\Rserve" + " folder. If not, install RServe using the following R script: install.packages (Rserve)";
					logger.error(msg1+details, e);
					e.printStackTrace();
					MessageManager.INSTANCE.add(Message.error(msg1+". " + details));
					return ValidationStatus.error(msg2, new Throwable(details));
				}

				return Status.OK_STATUS;
			}
		}.schedule();

		// Agrego un listener para cuando se baje la aplicación se ejecute
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				if (process != null) {
					try {
						// TODO mover a la API R4JSession?
						// shutdown remote Rserve. Note that some Rserves cannot
						// be shut down from the client side.
						new RConnection(RServeConfigurator.getInstance().getHost(), RServeConfigurator.getInstance().getPort())//
								.shutdown();
					} catch (RserveException e) {
						logger.error("error bajando el servidor Rserve", e);
					}

				}

			}

		});
	}
}
