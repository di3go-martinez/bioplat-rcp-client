package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.GenerateMetasignatureWizard;

public class OpenGenerateMetasignatureWizardAction extends IntroAction {

	@Override
	protected boolean run0() {
		return new GenerateMetasignatureWizard().open();
	}

}
