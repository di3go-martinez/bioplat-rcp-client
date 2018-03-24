package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.CreateRandomBiomarkerWizard;

public class CreateRandomGeneSignatureIntroAction extends IntroAction {

	@Override
	protected boolean run0() {

		return open(new CreateRandomBiomarkerWizard());
	}

}
