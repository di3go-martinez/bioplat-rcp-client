package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.List;

import edu.unlp.medicine.domainLogic.ext.experimentCommands.RemoveGenesGivingTheListOfGenesToRemoveCommand;
import edu.unlp.medicine.entity.gene.Gene;

public class RemoveGenesContribution extends FreeGeneInputActionContribution {
	@Override
	protected void executeOn(List<Gene> genes) {
		new RemoveGenesGivingTheListOfGenesToRemoveCommand(model(), genes).execute();
	}
}
