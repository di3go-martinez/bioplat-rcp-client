package edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.contributions;

import java.util.ArrayList;
import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.FreeGeneInputActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

public class RemoveGenes extends FreeGeneInputActionContribution<Biomarker> {

	// @Override
	// public void run() {
	// GenesInputDialog gin = new GenesInputDialog();
	// List<Gene> genes = Lists.newArrayList();
	// if (gin.accepted())
	// genes = gin.genes();
	//
	//
	// }

	@Override
	protected void executeOn(List<Gene> genes) {
		final MessageManager mmgr = MessageManager.INSTANCE;
		
		List<String> genesRemovedList=new ArrayList<String>(), genesNotInGSList=new ArrayList<String>();

		
		for (Gene g : genes) {
			if (!model().getGenes().contains(g))
				genesNotInGSList.add(g.getEntrezAsString());
				//mmgr.add(Message.warn("The Gene " + g + " is not in the gene signature " + model()));
			else {
				model().removeGenes(g);
				genesRemovedList.add(g.getEntrezAsString());
				//mmgr.add(Message.info("The Gene " + g + " was removed from the gene signature " + model()));
			}
		}
		if (genesRemovedList.size()>0) mmgr.add(Message.info("Genes removed from the Gene Signature: " + GUIUtils.getGeneListAsString(genesRemovedList)));
		if (genesNotInGSList.size()>0) mmgr.add(Message.warn("Genes not in the Gene Signature (they were discarded): " + GUIUtils.getGeneListAsString(genesNotInGSList)));


	}

}
