package edu.unlp.medicine.bioplat.rcp.intro.actions;

import org.eclipse.ui.internal.dialogs.ImportExportWizard;

/**
 * 
 * @author diego
 * @deprecated no se abre el contenedor de wizards directamente
 */
@Deprecated
public class OpenFileExperimentImportAction extends IntroAction {

	@SuppressWarnings("restriction")
	@Override
	public boolean run0() {

		ImportExportWizard wizard = new ImportExportWizard(ImportExportWizard.IMPORT);

		return open2(wizard);

	}

}
