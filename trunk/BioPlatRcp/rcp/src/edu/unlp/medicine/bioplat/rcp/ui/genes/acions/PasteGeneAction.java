package edu.unlp.medicine.bioplat.rcp.ui.genes.acions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;

import edu.unlp.medicine.bioplat.rcp.ui.genes.dialogs.GenesInputDialog;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Models;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

/**
 * 
 * @author diego
 * @deprecated migrar a handler
 */
@Deprecated
public class PasteGeneAction extends Action {
	public PasteGeneAction() {
		setText("Paste!");
	}

	@Override
	public void run() {

		MessageManager mm = MessageManager.INSTANCE;

		Biomarker b = getActiveBiomarker();
		if (b == null) {
			mm.add(Message.warn("No hay biomarcador seleccionado"));
			return;
		}

		GenesInputDialog mydialog = new GenesInputDialog(PlatformUIUtils.findShell());
		if (mydialog.open() == Dialog.OK)
			for (String id : mydialog.getids()) {
				try {
					final Gene gene = MetaPlat.getInstance().getGeneById(id);
					if (!b.getGenes().contains(gene)) {
						b.addGene(gene);
						mm.add(Message.info("Se agregó el gen: " + gene));
					} else
						mm.add(Message.warn("El gen " + gene + " ya estaba agregado al biomarcador"));
				} catch (Exception e) {
					mm.add(Message.error("No se pudo agregar el gen con id: '" + id + "'", e));
				}
			}

	}

	private Biomarker getActiveBiomarker() {
		return Models.getInstance().getActiveBiomarker();
	}

}
