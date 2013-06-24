package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.RemoveGenesGivingTheListOfGenesToKeepCommand;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

public class KeepOnlyContribution extends FreeGeneInputActionContribution<Experiment> {

	@Override
	protected void executeOn(List<Gene> genes) {
	
		boolean status = PlatformUIUtils.openQuestion("Filtering genes on experiment", "All other genes will be removed from experiment. If you want to get the hole experiment, you will need to import it again. Are you sure you want to continue?");
		if (status) new RemoveGenesGivingTheListOfGenesToKeepCommand(model(), genes).execute();
	}

}
