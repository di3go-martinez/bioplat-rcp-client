package edu.unlp.medicine.bioplat.rcp.intro.actions;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.GSEImport;

public class ImportFromInSilicoIntroAction extends IntroAction {

	@Override
	protected boolean run0() {

		return open(new GSEImport());
	}

}
