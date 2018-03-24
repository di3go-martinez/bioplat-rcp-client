package edu.unlp.medicine.bioplat.rcp.application;

import org.bioplat.r4j.R4JClient.connections.R4JConfigurator;
import org.bioplat.r4j.R4JClient.exceptions.R4JServerShutDownException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.r4jServer.BioplatR4JServer;
import edu.unlp.medicine.utils.fileSystem.BioplatFileSystemUtils;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(600, 500));
		configurer.setShowMenuBar(true);
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(true);
		configurer.setTitle("BioPlat"); //$NON-NLS-1$

		// acá seteo el flag para que se abra o no la vista de welcome
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_INTRO, true);
		// FIXME TODO sacar este discouraged access
		WorkbenchPlugin.getDefault().getPreferenceStore().setValue(IPreferenceConstants.RUN_IN_BACKGROUND, false);
		
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_MEMORY_MONITOR, true);
	}

	@Override
	public void postWindowOpen() {
		getWindowConfigurer().getWindow().getShell().setMaximized(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowShellClose()
	 */
	@Override
	public boolean preWindowShellClose() {
        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
        MessageBox messageBox = new MessageBox(PlatformUIUtils.findShell(), style);
        messageBox.setText("Information");
        messageBox.setMessage("Do you really want to exit?");
		return (messageBox.open() == SWT.YES);
	}
	
	
	@Override
	public void postWindowClose() {
		try {
			if (isRServerRunningLocal())
				BioplatR4JServer.getInstance().getServer().shutDown();
			BioplatFileSystemUtils.deleteImagesFolder();
		} catch (R4JServerShutDownException e) {
			logger.error(
					"Problem shutting down the Rserve on port: " + R4JConfigurator.getInstance().getPort(), e);
		}
		super.postWindowClose();
	}
	
	
	public static  boolean isRServerRunningLocal() {
		return "true".equals(R4JConfigurator.getInstance().getLocal().toLowerCase());
	}
	
	private Logger logger = LoggerFactory.getLogger(ApplicationWorkbenchWindowAdvisor.class);
	
}
