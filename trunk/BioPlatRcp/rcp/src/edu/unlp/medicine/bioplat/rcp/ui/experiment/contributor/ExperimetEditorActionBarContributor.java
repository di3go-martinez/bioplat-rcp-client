package edu.unlp.medicine.bioplat.rcp.ui.experiment.contributor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IEditorPart;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractEditorActionBarContributor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.GeneSearchDialog;
import edu.unlp.medicine.entity.experiment.Experiment;

public class ExperimetEditorActionBarContributor extends AbstractEditorActionBarContributor<Experiment> {

	private ExperimentEditor editor;

	@Override
	protected Class<?> getSelectionType() {
		return Experiment.class;
	}

	@Override
	protected void populateMenu(IMenuManager menu) {

		// menu.add(new Action() {
		// @Override
		// public void run() {
		// doRun();
		// }
		//
		// @Override
		// public String getText() {
		// return "find...";
		// }
		// });

	}

	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		this.editor = (ExperimentEditor) targetEditor;
	}

	private void doRun() {
		GeneSearchDialog gs = GeneSearchDialog.createDialog();

		if (gs.open() == GeneSearchDialog.OK) {
			editor.showGene(gs.selectedGene());
		}

	}
}
