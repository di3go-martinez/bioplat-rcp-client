package edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.contributions;

import java.util.List;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.genes.dialogs.GenesInputDialog;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

public class RemoveGenes extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		GenesInputDialog gin = new GenesInputDialog();
		List<Gene> genes = Lists.newArrayList();
		if (gin.accepted())
			genes = gin.genes();

		final MessageManager mmgr = MessageManager.INSTANCE;
		for (Gene g : genes) {
			if (!model().getGenes().contains(g))
				mmgr.add(Message.warn("The Gene " + g + " is not in the biomarker " + model()));
			else {
				model().removeGenes(g);
				mmgr.add(Message.info("The Gene " + g + " was removed from the biomarker " + model()));
			}
		}

	}

}
