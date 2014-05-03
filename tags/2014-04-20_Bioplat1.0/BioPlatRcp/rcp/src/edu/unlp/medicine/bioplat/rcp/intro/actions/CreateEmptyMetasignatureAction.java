package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.CreateEmptyBiomarkerWizard;

public class CreateEmptyMetasignatureAction extends IntroAction {

	@Override
	protected boolean run0() {

		return open(new CreateEmptyBiomarkerWizard());
	}

}
