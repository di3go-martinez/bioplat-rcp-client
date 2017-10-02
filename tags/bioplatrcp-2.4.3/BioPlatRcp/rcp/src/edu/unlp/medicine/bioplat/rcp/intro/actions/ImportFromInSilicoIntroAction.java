package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.ImportExperimentFromInSilicoWizard;

public class ImportFromInSilicoIntroAction extends IntroAction {

	@Override
	protected boolean run0() {

		return open(new ImportExperimentFromInSilicoWizard());
	}

}
