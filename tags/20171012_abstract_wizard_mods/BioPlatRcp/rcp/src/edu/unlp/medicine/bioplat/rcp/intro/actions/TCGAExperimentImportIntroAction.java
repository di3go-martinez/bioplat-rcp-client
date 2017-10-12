package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.ImportExperimentFromTCGATest;

public class TCGAExperimentImportIntroAction extends IntroAction {

	@Override
	protected boolean run0() {
		return open(new ImportExperimentFromTCGATest());
	}

}
