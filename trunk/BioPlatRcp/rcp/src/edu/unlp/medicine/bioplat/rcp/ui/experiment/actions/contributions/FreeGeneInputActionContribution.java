package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.Collections;
import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.genes.dialogs.GenesInputDialog;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

public abstract class FreeGeneInputActionContribution extends AbstractActionContribution<Experiment> {

	private static final List<Gene> EMPTY = Collections.emptyList();

	@Override
	public void run() {

		final Holder<List<Gene>> holder = Holder.create(EMPTY);

		PlatformUIUtils.findDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				GenesInputDialog agd = new GenesInputDialog();
				if (agd.accepted()) {
					holder.hold(agd.genes());
				}
			}

		});

		final List<Gene> value = holder.value();
		if (!value.isEmpty())
			executeOn(value);

	}

	/**
	 * Acción que se lleva a cabo sobre una lista de genes
	 * 
	 * @param genes
	 */
	protected abstract void executeOn(List<Gene> genes);
}
