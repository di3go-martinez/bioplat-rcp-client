package edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.contributions;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.TreeWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class TreeAC extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		inDisplay(new Runnable() {
			@Override
			public void run() {
				new TreeWizard(model()).blockOnOpen().open();
			}
		});
	}
}
