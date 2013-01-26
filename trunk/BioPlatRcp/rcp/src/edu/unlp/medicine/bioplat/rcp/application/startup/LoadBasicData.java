package edu.unlp.medicine.bioplat.rcp.application.startup;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;

import edu.unlp.medicine.domainLogic.framework.MetaPlat;

public class LoadBasicData implements IStartup {

	@Override
	public void earlyStartup() {
		// Sale un diálogo que bloquea la pantalla de usuario hasta que se
		// cargan todos los datos básicos

		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
				try {
					dialog.run(true, false, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) {
							letsStart(monitor);
						}
					});
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		// Job job = new UIJob("Initializing...") {
		//
		// protected IStatus run0(IProgressMonitor monitor) {
		// letsStart(monitor);
		//
		// return ValidationStatus.ok();
		// }
		//
		// @Override
		// public IStatus runInUIThread(IProgressMonitor monitor) {
		// return run0(monitor);
		// }
		// };
		//
		// job.schedule();

	}

	protected void letsStart(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Initializing data. Please wait.", IProgressMonitor.UNKNOWN);

			MetaPlat.getInstance().getARNMPLatforms();
		} finally {
			monitor.done();
		}
	}
}
