package edu.unlp.medicine.bioplat.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;

public class DummyActionContribution extends AbstractActionContribution<ExperimentAppliedToAMetasignature> {

	private static Logger logger = LoggerFactory.getLogger(DummyActionContribution.class);

	@Override
	public void run() {
		logger.info("Algo dummy sobre " + model());
	}

}
