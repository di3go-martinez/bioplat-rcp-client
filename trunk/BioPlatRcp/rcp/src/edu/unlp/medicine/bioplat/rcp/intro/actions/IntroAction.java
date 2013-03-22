package edu.unlp.medicine.bioplat.rcp.intro.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.ImportExportWizard;

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

	/**
	 * Abre el wizard recibido por parámetro
	 * 
	 * @param wizard
	 * @return true si el wizard se cerró
	 */
	protected boolean open(IWorkbenchWizard wizard) {
		WizardDialog d = new MyWizardDialog(PlatformUIUtils.findShell(), wizard);

		wizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);

		d.setBlockOnOpen(true);

		// TODO resolver con scrollbars
		d.setPageSize(400, 450);
		return d.open() == Dialog.OK;
	}

	/**
	 * // FIXME un toque sucio, sacado de WizardHandler
	 * 
	 * @param wizard
	 * @return
	 */
	protected boolean open2(ImportExportWizard wizard) {
		wizard.init(PlatformUIUtils.getWorkbench(), StructuredSelection.EMPTY);

		IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
		IDialogSettings wizardSettings = workbenchSettings.getSection("ImportExportAction"); //$NON-NLS-1$
		if (wizardSettings == null) {
			wizardSettings = workbenchSettings.addNewSection("ImportExportAction"); //$NON-NLS-1$
		}
		wizard.setDialogSettings(wizardSettings);
		wizard.setForcePreviousAndNextButtons(true);

		int result = new WizardDialog(PlatformUIUtils.findShell(), wizard).open();
		return result == WizardDialog.OK;
	}
}
