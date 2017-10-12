package edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.contributions;

import java.util.ArrayList;
import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.FreeGeneInputActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.FreeValidationsInputActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

public class RemoveValidations extends FreeValidationsInputActionContribution<Biomarker> {

	@Override
	protected void executeOn(List<Validation> validations) {
		final MessageManager mmgr = MessageManager.INSTANCE;
		
		List<String> validationsRemovedList = new ArrayList<String>(); 
		for (Validation v : validations) {
			model().getValidations().remove(v);
			validationsRemovedList.add(v.getName());
			
			//mmgr.add(Message.info("The Gene " + g + " was removed from the gene signature " + model()));
		}
		model().fireChangeOfValidations();
		
		if (validationsRemovedList.size()>0) mmgr.add(Message.info("Experiment removed from the Statistic Analisys: " + GUIUtils.getGeneListAsString(validationsRemovedList)));
	}

	

}
