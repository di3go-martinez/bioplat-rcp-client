package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.survComp.SurvCompGUIProvider;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.validation.SurvCompTestUsingManualClusterCommand;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.SurvCompTestCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class SManualClusteringActionContribution extends DoManualClusteringActionContribution {

	@Override
	protected void run0() {
		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				final ValidationConfigManualClusterWizard wizard = new ValidationConfigManualClusterWizard(model(), new SurvCompGUIProvider()) {

					// TODO no va este método!
					@Override
					protected OneBiomarkerCommand createCommand(Biomarker aBiomarker, ArrayList<ValidationConfig4DoingCluster> list) {
						return new SurvCompTestCommand(aBiomarker, list);
					}

					@Override
					protected void run(ValidationConfig4DoingCluster validationConfig) {
						//SurvCompExecutor.getInstance().execute(dummyBiomarker(), new SurvCompValidationConfig(validationConfig));
						 new SurvCompTestUsingManualClusterCommand(validationConfig).execute();
					}

					@Override
					protected String getResultMessage() {
						return "Concordnace index validation was sucessfully executed. You can see the results on the experiment 'Concordance index validations' tab";
					}

				};

				wizard.open();
			}
		});
	}

}
