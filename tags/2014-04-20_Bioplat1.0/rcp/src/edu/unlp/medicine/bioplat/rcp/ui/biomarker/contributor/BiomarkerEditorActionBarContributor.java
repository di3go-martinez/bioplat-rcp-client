package edu.unlp.medicine.bioplat.rcp.ui.biomarker.contributor;

import org.eclipse.jface.action.IMenuManager;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractEditorActionBarContributor;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class BiomarkerEditorActionBarContributor extends AbstractEditorActionBarContributor<Biomarker> {

	// private static final IAction ADD_GENES = MessageViewOpenAction.wrap(new
	// PasteGeneAction());

	public BiomarkerEditorActionBarContributor() {
	}

	
	
	// @Override
	// public void contributeToCoolBar(ICoolBarManager coolBarManager) {
	// ToolBarManager tbm = new ToolBarManager();
	// tbm.add(ADD_GENES);
	// coolBarManager.add(tbm);
	// }

	@Override
	protected Class<?> getSelectionType() {
		return Biomarker.class;
	}

	@Override
	protected void populateMenu(IMenuManager menu) {
		// menu.add(MessageViewOpenAction.wrap(new AddGeneAction()));
		// menu.add(ADD_GENES);
	}

}
