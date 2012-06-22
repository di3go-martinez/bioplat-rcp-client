package edu.unlp.medicine.bioplat.rcp.intro.actions;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.ImportExportWizard;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

public class OpenFileExperimentImportAction extends IntroAction {

	@SuppressWarnings("restriction")
	@Override
	public boolean run0() {

		// FIXME un toque sucio, sacado de WizardHandler
		ImportExportWizard wizard = new ImportExportWizard(ImportExportWizard.IMPORT);

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
