package edu.unlp.medicine.bioplat.rcp.ui.genes.view.acions;

import org.eclipse.jface.dialogs.Dialog;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.dialogs.GenesInputDialog;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

public class AddGenesAC extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {

		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				MessageManager mm = MessageManager.INSTANCE;

				Biomarker b = getActiveBiomarker();
				if (b == null) {
					mm.add(Message.warn("There is no gene signature selected"));
					return;
				}

				GenesInputDialog mydialog = new GenesInputDialog(PlatformUIUtils.findShell());
				if (mydialog.open() == Dialog.OK)
					for (String id : mydialog.getids()) {
						try {
							final Gene gene = MetaPlat.getInstance().findGene(id);
							if (!b.getGenes().contains(gene)) {
								b.addGene(gene);
								mm.add(Message.info("Gene " + gene + " added to " + b.getName() + "."));
							} else
								mm.add(Message.warn("The gene " + gene + " is already in the gene signature"));
						} catch (Exception e) {
							mm.add(Message.error("Couldnt add the gene with id '" + id + "'", e));
						}
					}

			}
		});

	}

	private Biomarker getActiveBiomarker() {
		return model();
	}

}
