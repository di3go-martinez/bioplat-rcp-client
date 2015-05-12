package edu.medicine.bioplat.rcp;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;

public class CalculateClusterActionContribution extends AbstractActionContribution<AbstractExperiment> {

	public CalculateClusterActionContribution() {
	}

	// public void run_() {
	//
	// AbstractExperiment exp = model();
	// Map<Sample, Integer> groups = Maps.newHashMap();
	//
	// for (Sample s : exp.getSamples()) {
	// final Random random = new Random();
	// if (random.nextBoolean())
	// groups.put(s, 1);
	// else
	// groups.put(s, 0);
	// }
	// exp.setGroups(groups);
	// }

	@Override
	public void run() {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				new ConfigureClusterDialog(model()).open();
			}
		});

	}

}
