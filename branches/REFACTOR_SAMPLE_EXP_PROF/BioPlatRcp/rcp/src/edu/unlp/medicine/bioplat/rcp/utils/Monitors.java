package edu.unlp.medicine.bioplat.rcp.utils;

import org.eclipse.core.runtime.IProgressMonitor;

import edu.unlp.medicine.utils.monitor.Monitor;

public class Monitors {

	/**
	 * 
	 * @param progressMonitor
	 * @return un nuevo monitor que delega el comportamiento en el
	 *         progressMonitor
	 */
	public static Monitor adapt(final IProgressMonitor progressMonitor) {
		return new Monitor() {
			private IProgressMonitor delegate = progressMonitor;

			@Override
			public void beginTask(String name, int totalWork) {
				delegate.beginTask(name, totalWork);
			}

			@Override
			public void done() {
				delegate.done();
			}

			@Override
			public void internalWorked(double work) {
				delegate.internalWorked(work);
			}

			@Override
			public boolean isCanceled() {
				return delegate.isCanceled();
			}

			@Override
			public void setCanceled(boolean value) {
				delegate.setCanceled(value);
			}

			@Override
			public void setTaskName(String name) {
				delegate.setTaskName(name);
			}

			@Override
			public void subTask(String name) {
				delegate.subTask(name);
			}

			@Override
			public void worked(int work) {
				delegate.worked(work);
			}
		};
	}
}
