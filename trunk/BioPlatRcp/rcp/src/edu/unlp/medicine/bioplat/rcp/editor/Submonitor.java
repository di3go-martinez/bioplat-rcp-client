package edu.unlp.medicine.bioplat.rcp.editor;

import org.eclipse.core.runtime.IProgressMonitor;

//TODO revisar...
public class Submonitor implements IProgressMonitor {
	@Override
	public void beginTask(String name, int totalWork) {
		parent.beginTask(name, totalWork);
	}

	@Override
	public void done() {
	}

	@Override
	public void internalWorked(double work) {
		parent.internalWorked(work);
	}

	@Override
	public boolean isCanceled() {
		return parent.isCanceled();
	}

	@Override
	public void setCanceled(boolean value) {

	}

	@Override
	public void setTaskName(String name) {
		parent.setTaskName(name);
	}

	@Override
	public void subTask(String name) {
		parent.subTask(name);
	}

	@Override
	public void worked(int work) {
		parent.worked(work);
	}

	private IProgressMonitor parent;

	public Submonitor(IProgressMonitor monitor) {
		this.parent = monitor;
	}
}
