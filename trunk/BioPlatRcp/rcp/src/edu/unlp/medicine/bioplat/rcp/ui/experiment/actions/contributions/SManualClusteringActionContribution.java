package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.LogRankTestExecutor;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.LogRankTestValidationConfig;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;

public class SManualClusteringActionContribution extends DoManualClusteringActionContribution {

	@Override
	protected void run0(ValidationConfig4DoingCluster validation) {
		LogRankTestExecutor.getInstance().execute(dummyBiomarker(), createValidationFor(validation));

	}

	private LogRankTestValidationConfig createValidationFor(ValidationConfig4DoingCluster validation) {
		return new LogRankTestValidationConfig(validation);
	}

}
