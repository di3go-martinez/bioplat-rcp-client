package edu.unlp.medicine.bioplat.rcp.intro.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

/**
 * 
 * Acción que se invoca exclusivamente desde la pantalla de welcome. Se encarga
 * de delegar en una subclase la ejecución de la acción, y cerrar la vista de
 * welcome
 * 
 * @author diego martínez
 * 
 */
public abstract class IntroAction extends Action {

	@Override
	public final void run() {
		if (run0()) {
			IWorkbenchPage page = PlatformUIUtils.activePage();
			// page.setPartState(page.findViewReference("org.eclipse.ui.internal.introview"),
			// IWorkbenchPage.STATE_MINIMIZED);
			page.hideView(PlatformUIUtils.findView("org.eclipse.ui.internal.introview"));
		}
	}

	/**
	 * 
	 * @return true si se cierra la página de welcome, false si no
	 */
	protected abstract boolean run0();
}
