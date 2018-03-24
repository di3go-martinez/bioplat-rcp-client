/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;
import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.validationTest.ValidationGUIProvider;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.ValidationsTestCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * @author Juan
 * 
 */
public class ApplyValidationTestWizard extends ValidationConfigWizard {

	/**
	 * @param biomarker
	 * @param acceptRange
	 */
	public ApplyValidationTestWizard(Biomarker biomarker, boolean acceptRange) {
		super(biomarker, acceptRange, new ValidationGUIProvider());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.
	 * ValidationConfigWizard
	 * #createCommand(edu.unlp.medicine.entity.biomarker.Biomarker,
	 * java.util.List)
	 */
	@Override
	public List<OneBiomarkerCommand> createCommand(Biomarker findBiomarker,
			List<ValidationConfig4DoingCluster> list) {
		List<OneBiomarkerCommand> commandList = new ArrayList<OneBiomarkerCommand>();
		commandList.add(new ValidationsTestCommand(findBiomarker, list));		
		return commandList;

	}

}
