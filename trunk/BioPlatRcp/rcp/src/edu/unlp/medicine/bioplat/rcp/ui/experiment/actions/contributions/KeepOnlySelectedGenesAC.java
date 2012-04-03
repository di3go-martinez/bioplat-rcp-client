package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.List;

import edu.unlp.medicine.domainLogic.ext.experimentCommands.RemoveGenesGivingTheListOfGenesToKeepCommand;
import edu.unlp.medicine.entity.gene.Gene;

public class KeepOnlySelectedGenesAC extends RemoveSelectedGenesActionContribution {
	@Override
	protected void executeOn(List<Gene> selectedGenes) {
		new RemoveGenesGivingTheListOfGenesToKeepCommand(model(), selectedGenes).execute();
	}
}
