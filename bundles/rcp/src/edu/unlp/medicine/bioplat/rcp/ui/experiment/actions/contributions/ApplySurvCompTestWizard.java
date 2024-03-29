package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.survComp.SurvCompGUIProvider;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.SurvCompTestCommand;
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
public class ApplySurvCompTestWizard extends ValidationConfigWizard {

	public ApplySurvCompTestWizard(Biomarker biomarker, boolean acceptRange) {
		super(biomarker, acceptRange, new SurvCompGUIProvider());

	}

	@Override
	public List<OneBiomarkerCommand> createCommand(Biomarker biomarker,
			List<ValidationConfig4DoingCluster> validationConfigs) {
		List<OneBiomarkerCommand> commandList = new ArrayList<OneBiomarkerCommand>();
		commandList.add(new SurvCompTestCommand(biomarker, validationConfigs));
		return commandList;

	}

}
