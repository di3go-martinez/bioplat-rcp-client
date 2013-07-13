package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.logRankTest.LogRankTestGUIProvider;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.LogRankTestCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * Wizard de creación de ValidationConfigs para la aplicación de experimentos a
 * biomarcadores
 * 
 * @author diego martínez
 * 
 */
public class ApplyLogRankTestWizard extends ValidationConfigWizard {


	public ApplyLogRankTestWizard(Biomarker biomarker, boolean acceptRange) {
		super(biomarker, acceptRange, new LogRankTestGUIProvider());
		
	}

	@Override
	public OneBiomarkerCommand createCommand(Biomarker biomarker,
			ArrayList<ValidationConfig4DoingCluster> validationConfigs) {
		 
		return new LogRankTestCommand(biomarker, validationConfigs);
		
	}


}
