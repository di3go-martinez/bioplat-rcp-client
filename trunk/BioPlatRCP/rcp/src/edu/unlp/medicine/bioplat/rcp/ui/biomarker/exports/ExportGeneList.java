package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import edu.unlp.medicine.bioplat.rcp.ui.genes.acions.Models;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

public class ExportGeneList extends Wizard implements IExportWizard {

    public ExportGeneList() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {

        addPage(new WizardPage("p1") {

            @Override
            public void createControl(Composite parent) {
                Label label = new Label(parent, SWT.BORDER);
                label.setText("exportar genes");
                setControl(label);
            }

        });
    }

    private Biomarker b;

    @Override
    public boolean performFinish() {
        if (b == null) return false;

        for (Gene g : b.getGenes())
            System.out.println(g);

        return true;
    }

    @Override
    public boolean canFinish() {
        b = Models.getInstance().getActiveBiomarker();
        return b != null;
    }
}
