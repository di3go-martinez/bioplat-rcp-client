package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;
import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.logRankTest.LogRankTestGUIProvider;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.validation.LogRankTestUsingManualClusterCommand;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.LogRankTestCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.LogRankTestValidationConfig;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.LogRankTestValidationResult;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class KManualClusteringActionContribution extends DoManualClusteringActionContribution {

	@Override
	protected void run0() {
		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				final ValidationConfigManualClusterWizard wizard = new ValidationConfigManualClusterWizard(model(), new LogRankTestGUIProvider()) {

					// TODO no va este método!
					@Override
					protected OneBiomarkerCommand createCommand(Biomarker aBiomarker, ArrayList<ValidationConfig4DoingCluster> list) {
						return new LogRankTestCommand(aBiomarker, list);
					}

					@Override
					protected void run(ValidationConfig4DoingCluster validationConfig) {
						//LogRankTestExecutor.getInstance().execute(dummyBiomarker(), new LogRankTestValidationConfig(validationConfig));
						new LogRankTestUsingManualClusterCommand(validationConfig).execute();
						List<LogRankTestValidationResult> res = validationConfig.getExperimentToValidate().getLogRankTestValidationResults();
						System.out.println(res.size());
					}

				};

				wizard.open();
			}
		});

	}

	private LogRankTestValidationConfig createValidationFor(ValidationConfig4DoingCluster validation) {
		return new LogRankTestValidationConfig(validation);
	}

}
