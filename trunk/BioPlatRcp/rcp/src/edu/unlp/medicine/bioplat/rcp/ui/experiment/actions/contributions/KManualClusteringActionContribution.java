package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.SurvCompValidationConfig;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.tests.SurvCompExecutor;

public class KManualClusteringActionContribution extends DoManualClusteringActionContribution {

	@Override
	protected void run0(ValidationConfig4DoingCluster validation) {
		SurvCompExecutor.getInstance().execute(dummyBiomarker(), createValidationFor(validation));

	}

	private SurvCompValidationConfig createValidationFor(ValidationConfig4DoingCluster validation) {
		return new SurvCompValidationConfig(validation);
	}

}
