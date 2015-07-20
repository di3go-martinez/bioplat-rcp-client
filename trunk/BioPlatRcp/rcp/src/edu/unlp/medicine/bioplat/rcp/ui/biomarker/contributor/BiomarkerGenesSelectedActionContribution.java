package edu.unlp.medicine.bioplat.rcp.ui.biomarker.contributor;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.FreeGeneInputActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.dialogs.BiomarkerFromBiomarkerInputDialog;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public abstract class BiomarkerGenesSelectedActionContribution<T extends AbstractEntity> extends AbstractActionContribution<T> {
	private static Logger logger = LoggerFactory.getLogger(FreeGeneInputActionContribution.class);

	private static final List<Gene> EMPTY = Collections.emptyList();
	
	@Override
	public void run() {

		// FIXME Chatcheo el nullpointer porque pasa a veces...
		try {
			final Holder<List<Gene>> holder = Holder.create(EMPTY);
			

			PlatformUIUtils.findDisplay().syncExec(new Runnable() {
				@Override
				public void run() {

					BiomarkerFromBiomarkerInputDialog agd = new BiomarkerFromBiomarkerInputDialog().genes(selectedGenes());
					if (agd.accepted()){
						holder.hold(agd.genes());
					}
				}

				private List<Gene> selectedGenes() {
					MultipleSelection ms = getSelection();
					return ms.get(Constants.SELECTED_GENES).toList();
				}

			});

			final List<Gene> genes = holder.value();
			if (!genes.isEmpty())
				executeOn(genes);
			else
				MessageManager.INSTANCE.add(Message.warn("You provide an empty gene list."));
		} catch (NullPointerException npe) {
			logger.warn("nullpointer en la acción, catcheado apropósito");
		}
	}

	/**
	 * Acción que se lleva a cabo sobre una lista de genes
	 * 
	 * @param genes
	 */
	protected abstract void executeOn(List<Gene> genes);
	
}
