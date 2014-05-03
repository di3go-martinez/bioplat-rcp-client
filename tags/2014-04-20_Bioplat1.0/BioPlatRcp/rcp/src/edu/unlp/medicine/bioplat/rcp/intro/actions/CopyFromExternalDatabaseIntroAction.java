package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.from.gene.signature.db.FromImportedGeneSignatureWizard;

public class CopyFromExternalDatabaseIntroAction extends IntroAction {

	@Override
	protected boolean run0() {
		return open(new FromImportedGeneSignatureWizard());
	}

}
