package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.survComp.SurvCompGUIProvider;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.validation.SurvCompTestUsingManualClusterCommand;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.SurvCompTestCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.WaysToGetBiomarkerEnum;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationManager;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.rAPI.RValidator;
import edu.unlp.medicine.domainLogic.framework.statistics.clusterers.RClustererManualSetting;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.ClusteringResult;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.EditedBiomarker;
import edu.unlp.medicine.entity.experiment.ClusterData;

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
					protected void run(ValidationConfig4DoingCluster validationConfig4DoingCluster) {
						//SurvCompExecutor.getInstance().execute(dummyBiomarker(), new SurvCompValidationConfig(validationConfig));
						
						model().getValidationManager().getBiomarker().setGenes(validationConfig4DoingCluster.getExperimentToValidateWithNoNullSamples().getGenes());
						
						validationConfig4DoingCluster.setClusterer(new RClustererManualSetting());
						validationConfig4DoingCluster.setNumberOfClusters(validationConfig4DoingCluster.getClusteringResult().getNumberOfClusters());
						ValidationConfig validationConfig = new ValidationConfig(validationConfig4DoingCluster);
						
						//RValidator rvalidator = new RValidator();
						//Validation validation = rvalidator.validate(model().getValidationManager().getBiomarker(), validationConfig);
						model().getValidationManager().getBiomarker().validate(validationConfig);
						model().getValidationManager().getBiomarker().fireChangeOfValidations();
						
						//new SurvCompTestUsingManualClusterCommand(validationConfig).execute();
					}

					@Override
					protected String getResultMessage() {
						return "Manual settings validation was sucessfully executed. You can see the results on the experiment 'Statistic Analysis' tab";
					}

				};

				wizard.open();
			}
		});
	}

}
