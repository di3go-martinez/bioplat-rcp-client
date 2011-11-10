package edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions;

import ognl.Ognl;
import ognl.OgnlException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.BiomarkerEditor;
import edu.unlp.medicine.bioplat.rcp.ui.genes.acions.Models;
import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.EditedBiomarker;

public class OpenBiomarkerAction implements IWorkbenchWindowActionDelegate, ISelectionListener {

    @Override
    public void run(IAction action) {
        Biomarker b = createBiomarker();
        PlatformUIUtils.openEditor(EditorInputFactory.createDefaultEditorInput(b), BiomarkerEditor.id());
    }

    private Biomarker createBiomarker() {
        Biomarker b = new EditedBiomarker();
        // GeneSearch gs = GeneSearch.createDialog();
        // if (gs.open() == Dialog.OK) b.addGene(gs.selectedGene());

        return b;
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        System.out.println("TODO selectionChanged!");
    }

    @Override
    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    private IWorkbenchWindow window;

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        try {
            Biomarker b = (Biomarker) Ognl.getValue("biomarker", selection);
            Models.getInstance().setActiveBiomarker(b);
        } catch (OgnlException e) {

        }
    }
}
