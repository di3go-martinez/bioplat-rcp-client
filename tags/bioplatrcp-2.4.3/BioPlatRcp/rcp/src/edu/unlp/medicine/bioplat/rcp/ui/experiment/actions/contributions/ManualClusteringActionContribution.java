package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.logRankTest.LogRankTestGUIProvider;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class ManualClusteringActionContribution extends DoManualClusteringActionContribution {
	

	@Override
	protected void run0() {
		PlatformUIUtils.findDisplay().syncExec(new Runnable() {
			

			@Override
			public void run() {
				final ValidationConfigManualClusterWizard wizard = new ValidationConfigManualClusterWizard(model(), new LogRankTestGUIProvider()) {

					@Override
					protected String getResultMessage() {
						// TODO Auto-generated method stub
						return ""; // TODO que texto iria aca.
					}

					@Override
					protected void run(	ValidationConfig4DoingCluster validationConfig) {
						// TODO Auto-generated method stub
						
					}

					@Override
					protected OneBiomarkerCommand createCommand(Biomarker biomarker,ArrayList<ValidationConfig4DoingCluster> list) {
						// TODO Auto-generated method stub
						return null;
					}
						
				};
		
				wizard.open();
			
			}
				
		});
	}

}
