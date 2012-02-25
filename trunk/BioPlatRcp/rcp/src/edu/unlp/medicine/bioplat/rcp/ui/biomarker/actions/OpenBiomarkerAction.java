package edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Models;
import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.EditedBiomarker;

/**
 * 
 * @author diego
 * 
 */
@Deprecated
// TODO implementar el ISelectionListener en otro lado, si es necesario
public class OpenBiomarkerAction implements IWorkbenchWindowActionDelegate, ISelectionListener {

	@Override
	public void run(IAction action) {
		Biomarker b = createBiomarker();
		PlatformUIUtils.openEditor(EditorInputFactory.createDefaultEditorInput(b), EditorsId.biomarkerEditorId());
	}

	private Biomarker createBiomarker() {
		Biomarker b = new EditedBiomarker("");
		// GeneSearch gs = GeneSearch.createDialog();
		// if (gs.open() == Dialog.OK) b.addGene(gs.selectedGene());

		return b;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		System.out.println(OpenBiomarkerAction.class + " says selectionChanged! current selection: " + selection);
	}

	@Override
	public void dispose() {
		getSelectionService().removeSelectionListener(this);
	}

	private IWorkbenchWindow window;

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
		getSelectionService().addSelectionListener(this);
	}

	private ISelectionService getSelectionService() {
		return window.getSelectionService();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// try {
		// TODO sacar esto
		if (selection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			if (firstElement instanceof Biomarker) {
				Biomarker b = // (Biomarker) Ognl.getValue("biomarker",
								// selection);
				(Biomarker) firstElement;
				Models.getInstance().setActiveBiomarker(b);
			}
		}
		// } catch (OgnlException e) {
		// e.printStackTrace();
		// }
	}
}
