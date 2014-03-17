package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.CelFileExperimentImport;

public class CelFileExperimentImportIntroAction extends IntroAction {

	@Override
	protected boolean run0() {
		return open(new CelFileExperimentImport());
	}

}
