package edu.unlp.medicine.bioplat.rcp.ui.biomarker.contributor;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractEditorActionBarContributor;
import edu.unlp.medicine.bioplat.rcp.ui.genes.acions.AddGeneAction;
import edu.unlp.medicine.bioplat.rcp.ui.genes.acions.PasteGeneAction;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class BiomarkerEditorActionBarContributor extends AbstractEditorActionBarContributor<Biomarker> {

	public BiomarkerEditorActionBarContributor() {
	}

	@Override
	public void contributeToCoolBar(ICoolBarManager coolBarManager) {
		coolBarManager.add(ActionFactory.SAVE.create(getPage().getWorkbenchWindow()));
	}

	@Override
	protected Class<?> getSelectionType() {
		return Biomarker.class;
	}

	@Override
	protected void populateMenu(IMenuManager menu) {
		menu.add(new AddGeneAction());
		menu.add(new PasteGeneAction());
	}

}
