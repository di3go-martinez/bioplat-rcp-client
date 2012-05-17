package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.List;

import edu.unlp.medicine.domainLogic.ext.experimentCommands.RemoveGenesGivingTheListOfGenesToKeepCommand;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

public class KeepOnlyContribution extends FreeGeneInputActionContribution<Experiment> {

	@Override
	protected void executeOn(List<Gene> genes) {
		new RemoveGenesGivingTheListOfGenesToKeepCommand(model(), genes).execute();
	}

}
