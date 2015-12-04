package edu.unlp.medicine.bioplat.rcp.application.startup;

import java.util.List;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.ui.IStartup;
import org.jfree.ui.UIUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.r4j.exceptions.R4JServerShutDownException;
import edu.unlp.medicine.r4j.rLibrary.RLibrary;
import edu.unlp.medicine.r4j.server.ConnectionProperties;
import edu.unlp.medicine.r4j.systemProperties.R4JSystemPropertiesExpected;
import edu.unlp.medicine.r4jServer.BioplatR4JServer;
import edu.unlp.medicine.utils.fileSystem.BioplatFileSystemUtils;

public class StartupRserve implements IStartup {
	// Logger Object
	private static Logger logger = LoggerFactory.getLogger(StartupRserve.class);

	@Override
	public void earlyStartup() {

		new Job("Rserve - Initializing...") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Initializing rServe (bridge between Bioplat and R) and checking R Libraries...",
						IProgressMonitor.UNKNOWN);

				/**
				 * It initialize the BioplatR4JServer.
				 */
				BioplatR4JServer bioplatR4JServer = BioplatR4JServer.create(checkConnectionProperties());

				if (bioplatR4JServer.isStarted()) {
					if (bioplatR4JServer.getRequiredRLibrariesNotInstalled().size() == 0) {
						String okMessage = "All the R libraries required by Bioplat were succesfully loaded";
						MessageManager.INSTANCE.add(Message.info(okMessage));
						return ValidationStatus.OK_STATUS;
					} else
						return manageRequiredRLibsNotInstalled(bioplatR4JServer.getRequiredRLibrariesNotInstalled());
				} else {
					String errorMessage = "The RServer could not be started. You can use Bioplat but you will not be able to execute any of the statistics operations";
					MessageManager.INSTANCE.add(Message.error(errorMessage));
					return ValidationStatus.error(errorMessage);
				}

			}

			private ConnectionProperties checkConnectionProperties() {
				MyHttpClient c = new MyHttpClient();
				if (!c.connectionAvailable())
					return promptForProxy();
				else
					return ConnectionProperties.NONE;
			}

			private ConnectionProperties promptForProxy() {
				final Holder<String> holder = Holder.create();
				PlatformUIUtils.findDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						InputDialog id = new InputDialog(null, "Bioplat Proxy Settings",
								"We cannot connect to the Internet. Please configure a proxy to use", "proxyHost:port",
								new IInputValidator() {

							@Override
							public String isValid(String newText) {
								if (!check(newText))
									return "The text must be proxyHost:port";
								return null;// no error...
							}

							private boolean check(String newText) {
								String[] parts = newText.split(":");
								return newText.contains(":") && (parts[0] != null && !parts[0].trim().isEmpty())
										&& (org.apache.commons.lang.math.NumberUtils.isNumber(parts[1]));

							}
						});
						id.setBlockOnOpen(true);
						id.open();
						holder.hold(id.getValue());
					}
				});
				String[] parts = holder.value().split(":");//acá llega ok, porque lo valida el diálogo!
				String host = parts[0];
				int port = Integer.valueOf(parts[1]);
				return new ConnectionProperties(host, port);
			}

			private IStatus manageRequiredRLibsNotInstalled(List<RLibrary> rLibs) {
				StringBuilder rlibsSB = new StringBuilder("");

				for (RLibrary rLibrary : rLibs) {
					String lib = rLibrary.getName() + ": " + rLibrary.getInstallation();
					rlibsSB.append(lib);
					rlibsSB.append("\n");
				}
				MessageManager.INSTANCE.add(Message
						.error("There are some Bioplat required R libraries not installed in your R environment. Please, check the R environment you Bioplat is using ( "
								+ System.getProperty(R4JSystemPropertiesExpected.R_HOME_BIOPLAT_PROPERTY) + ")."
								+ "The list of non installed libraries and the script for installing them are: \n"
								+ rlibsSB));
				return ValidationStatus.warning(
						"There are some Bioplat required libraries not installed in your r environment. Take a look at the message view for more information");

			}

		}.schedule();

		// Agrego un listener para cuando se baje la aplicación se ejecute
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					BioplatR4JServer.getInstance().getServer().shutDown();
					BioplatFileSystemUtils.deleteImagesFolder();
				} catch (R4JServerShutDownException e) {
					logger.error("Problem shutting down the Rserve on port: "
							+ BioplatR4JServer.getInstance().getServer(),e);
				} catch (Throwable e) {
					logger.error("Error", e);
				}
			}
		});
	}
}
