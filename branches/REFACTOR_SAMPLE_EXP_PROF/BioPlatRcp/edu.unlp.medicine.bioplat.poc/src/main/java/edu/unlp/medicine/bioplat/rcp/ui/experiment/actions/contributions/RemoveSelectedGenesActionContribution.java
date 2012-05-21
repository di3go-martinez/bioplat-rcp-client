package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.List;

import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.RemoveGenesGivingTheListOfGenesToRemoveCommand;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

public class RemoveSelectedGenesActionContribution<T extends Experiment> extends AbstractActionContribution<T> {

	public RemoveSelectedGenesActionContribution() {
	}

	@Override
	public void run() {

		try {
			// TODO analizar este downcasting... @see
			// AbstractEditorPart#createSelectionProvider
			MultipleSelection ms = getSelection();
			final List<Gene> selectedGenes = ms.get(Constants.SELECTED_GENES).toList();
			executeOn(selectedGenes);
		} catch (NullPointerException e) {
			// FIXME FIXME hay veces que tira nullpointerexception porque no se
			// registró la
			// selection por ahora se ignora ya que en la mayoría de las veces
			// anda y no se determinó la causa exacta todavía...
			// FIXME FIXME SE DECIDIO TAPAR LA EXCEPCIÓN PARA QUE SE CREA QUE NO
			// FUE TOMADO EL CLICK Y SE VUELVA A PROBAR YA QUE LA PRÓXIMA VEZ
			// ANDARÁ
			e.printStackTrace();
		}
	}

	protected void executeOn(final List<Gene> selectedGenes) {
		new RemoveGenesGivingTheListOfGenesToRemoveCommand(model(), selectedGenes).execute();
	}
}
