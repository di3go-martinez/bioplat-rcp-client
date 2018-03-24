package edu.unlp.medicine.bioplat.rcp.utils;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

public enum Service {

	INSTANCE;

	public IProgressService getProgressService() {
		return PlatformUI.getWorkbench().getProgressService();
		// return
		// PlatformUIUtils.activePage().getWorkbenchWindow().getWorkbench().getProgressService();
	}

}
