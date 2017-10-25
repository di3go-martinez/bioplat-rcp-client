package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.cloud.ImportDatasetFromCloudWizard;

public class ImportDatasetFromBioplatCloud extends IntroAction {

	@Override
	protected boolean run0() {
		return open(new ImportDatasetFromCloudWizard());
	}

}
