package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.split;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.Experiment;

public class SplitDatasetOperation extends AbstractActionContribution<Experiment> {

	@Override
	public void run() {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				SplitterDialog.create(model()).open();
			}
		});
	}

}
