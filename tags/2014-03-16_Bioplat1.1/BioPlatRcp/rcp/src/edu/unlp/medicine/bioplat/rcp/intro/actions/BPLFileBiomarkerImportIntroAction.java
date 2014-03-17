package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.ImportGeneSignatureFromBPLWizard;

public class BPLFileBiomarkerImportIntroAction extends IntroAction {

	@Override
	protected boolean run0() {
		return open(new ImportGeneSignatureFromBPLWizard());
	}

}
