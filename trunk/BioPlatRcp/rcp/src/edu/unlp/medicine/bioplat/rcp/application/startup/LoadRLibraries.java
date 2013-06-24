package edu.unlp.medicine.bioplat.rcp.application.startup;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

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
import edu.unlp.medicine.bioplat.rcp.utils.RLibrary;
import edu.unlp.medicine.bioplat.rcp.utils.RLibraryManager;
import edu.unlp.medicine.bioplat.rcp.utils.RLibraryScanner;
import edu.unlp.medicine.r4j.constants.OSDependentConstants;
import edu.unlp.medicine.r4j.environments.RServeConfigurator;

public class LoadRLibraries implements IStartup {
	// Logger Object
	private static Logger logger = LoggerFactory.getLogger(LoadRLibraries.class);

	@Override
	public void earlyStartup() {

		new Job("Loading R Libraries...") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Loading R Libraries", IProgressMonitor.UNKNOWN);
				RLibraryManager rlibraryManager = new RLibraryManager();
				String msg;
				try {
					//Thread.sleep(5000);
					if (!rlibraryManager.isTheRserveRunning()) {
						//It is not ok, but it doesnt return validation.error because it is informed again. The best solution should be to not execute this startup if the StartupRServe failed. 
						return Status.OK_STATUS;
					}
					List<RLibrary> librariesNotInstalled = this.validateRLibrary(rlibraryManager);
					if (!librariesNotInstalled.isEmpty()) {
						msg = "Some required R packages are not installed (see message view).";
						String details = "Copy the script and paste it in the R environment\n\n";
						Iterator<RLibrary> iterator = librariesNotInstalled.iterator();
						while (iterator.hasNext()) {
							details = details.concat(iterator.next().getInstallation() + OSDependentConstants.LINE_SEPARATOR);
						}
						return ValidationStatus.error(msg, new Throwable(details));
					}
					// install libraries
					// this.validateInstallationOfLibraries(rlibraryManager,
					// librariesNotInstalled);
//				} catch (InterruptedException e) {
//					logger.error(e.getMessage());
//					MessageManager.INSTANCE.add(Message.error("Failed to validate the installation of the libraries"));
//					e.printStackTrace();
				} catch (FileNotFoundException e) {
					logger.error(e.getMessage());
					MessageManager.INSTANCE.add(Message.error("Failed to validate the installation of the libraries"));
					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}

			private void validateInstallationOfLibraries(RLibraryManager rlibraryManager, List<RLibrary> librariesNotInstalled) {
				librariesNotInstalled = rlibraryManager.installRLibraries(librariesNotInstalled);
				Iterator<RLibrary> librariesNotInstalledIterator = librariesNotInstalled.iterator();
				RLibrary library;
				while (librariesNotInstalledIterator.hasNext()) {
					library = librariesNotInstalledIterator.next();
					MessageManager.INSTANCE.add(Message.error("Open R environment and follow these instructions: " + library.getInstallation()));
				}
			}

			private List<RLibrary> validateRLibrary(RLibraryManager rlibraryManager) throws FileNotFoundException {
				RLibraryScanner scanner = new RLibraryScanner();
				Iterator<RLibrary> libraries = scanner.processLineByLine(this.getClass().getResourceAsStream("/resources/R/librariesToBioplat.r")).iterator();
				List<RLibrary> librariesNotInstalled = rlibraryManager.getLibrariesNotInstalled(libraries);
				Iterator<RLibrary> librariesNotInstalledIterator = librariesNotInstalled.iterator();
				while (librariesNotInstalledIterator.hasNext()) {
					// TODO por qué no intentar instalar las librerías que
					// faltan automáticamente??
					final RLibrary library = librariesNotInstalledIterator.next();
					logger.warn("The library " + library.getName() + " seems not to be present in R. Try to install it by running: " + library.getInstallation());
					MessageManager.INSTANCE.add(Message.error("The " + library.getName() + " library is not installed. Install it in R because it is necessary for Bioplat to work properly."));
				}
				return librariesNotInstalled;
			}

		}.schedule();

	}
}
