package edu.unlp.medicine.bioplat.rcp.ui.biomarker.contributor;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

import edu.unlp.medicine.bioplat.rcp.ui.genes.acions.AddGenAction;

public class BiomarkerEditorContributor extends EditorActionBarContributor {

	public BiomarkerEditorContributor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void contributeToCoolBar(ICoolBarManager coolBarManager) {
		coolBarManager.add(ActionFactory.SAVE.create(getPage()
				.getWorkbenchWindow()));
	}

    @Override
    public void contributeToMenu(IMenuManager menuManager) {
        super.contributeToMenu(menuManager);
        MenuManager mm = new MenuManager("Operaciones");
        mm.add(new AddGenAction());
        menuManager.add(mm);

    }

}
