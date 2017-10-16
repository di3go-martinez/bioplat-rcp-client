package edu.unlp.medicine.bioplat.rcp.application;

import static org.eclipse.ui.PlatformUI.RETURN_OK;
import static org.eclipse.ui.PlatformUI.RETURN_RESTART;
import static org.eclipse.ui.PlatformUI.createAndRunWorkbench;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {
		
		initializeCommandLineArguments();
	/*	
		if(!isWinOS() && xulRunnerIsUndefinied())		{
			System.setProperty("org.eclipse.swt.browser.XULRunnerPath",  defaultXULRunnerPath()); 
			System.setProperty("org.eclipse.swt.browser.DefaultType", "mozilla");
		}
		*/
		
		Display display = PlatformUI.createDisplay();
		try {
			int returnCode = RETURN_OK;
			// if (doLogin(display))
			returnCode = createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			return (returnCode == RETURN_RESTART) ? EXIT_RESTART : EXIT_OK;
		} finally {
			display.dispose();
		}

	}

	private String defaultXULRunnerPath() {
		return System.getProperty("user.dir") + "/xulrunner";
	}

	private boolean xulRunnerIsUndefinied() {
		return System.getProperty("org.eclipse.swt.browser.XULRunnerPath", null) == null;
	}

	private boolean isWinOS() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}

	private void initializeCommandLineArguments() {
		edu.unlp.medicine.utils.parameters.ApplicationParametersHolder.initialize(Platform.getCommandLineArgs());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}