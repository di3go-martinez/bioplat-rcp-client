package edu.unlp.medicine.bioplat.rcp.ui.experiment.contributor;

import org.eclipse.jface.action.IMenuManager;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractEditorActionBarContributor;
import edu.unlp.medicine.entity.experiment.Experiment;

public class ExperimetEditorActionBarContributor extends AbstractEditorActionBarContributor<Experiment> {

	@Override
	protected Class<?> getSelectionType() {
		return Experiment.class;
	}

	@Override
	protected void populateMenu(IMenuManager menu) {

	}

}
