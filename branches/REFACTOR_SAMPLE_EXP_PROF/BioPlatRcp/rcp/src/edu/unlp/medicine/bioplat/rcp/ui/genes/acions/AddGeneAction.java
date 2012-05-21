package edu.unlp.medicine.bioplat.rcp.ui.genes.acions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.genes.GeneSearch;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

/**
 * 
 * @author diego
 * @deprecated migrar a handler
 * 
 *             FIXME está mal hecha... se instancia dos veces por editor...
 */
// TODO extends y/o implements
@Deprecated
public class AddGeneAction extends Action implements IWorkbenchWindowActionDelegate {

	public AddGeneAction() {
		setText("Agregar gen");

	}

	// FIXME horrible parche, no debería haber distintas acciones...
	private static Biomarker b;

	@Override
	public void run() {
		MessageManager mm = MessageManager.INSTANCE;

		if (b == null) {
			mm.add(Message.warn("No hay biomarcador seleccionado"));
			return;
		}

		GeneSearch dialog = GeneSearch.createDialog();
		if (dialog.open() == Dialog.OK)
			try {
				final Gene selectedGene = dialog.selectedGene();
				if (!b.getGenes().contains(selectedGene)) {
					b.addGene(selectedGene);
					mm.add(Message.info("Gen agregado"));
				} else
					mm.add(Message.warn("El gen " + selectedGene + " ya estaba agregado al biomarcador"));
			} catch (Exception e) {
				mm.add(Message.error("No se encontró el gen buscado"));
			}
	}

	@Override
	public void run(IAction action) {
		run();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (!selection.isEmpty()) {
			Object theselection = null;
			if (selection instanceof MultipleSelection)
				theselection = ((MultipleSelection) selection).get(Constants.MODEL).getFirstElement();
			else if (selection instanceof IStructuredSelection)
				theselection = ((IStructuredSelection) selection).getFirstElement();
			if (theselection instanceof Biomarker)
				b = (Biomarker) theselection;
		}

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
