package edu.unlp.medicine.bioplat.rcp.ui.genes.acions;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import edu.unlp.medicine.bioplat.rcp.ui.genes.GeneSearch;
import edu.unlp.medicine.entity.biomarker.Biomarker;

//TODO extends y/o implements
public class AddGenAction extends Action implements IWorkbenchWindowActionDelegate {

    public AddGenAction() {
        setText("Agregar gen");
    }

    @Override
    public void run() {
        Biomarker b = Models.getInstance().getActiveBiomarker();

        if (b == null) return;

        GeneSearch dialog = GeneSearch.createDialog();
        if (dialog.open() == Dialog.OK) {
            b.addGene(dialog.selectedGene());
        }

    }

    @Override
    public void run(IAction action) {

        run();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

}
