package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import edu.unlp.medicine.bioplat.rcp.ui.genes.acions.Models;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class ExportToFile extends Wizard implements IExportWizard {

    public ExportToFile() {
        // TODO Auto-generated constructor stub
    }

    private String text;

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        addPage(new WizardPage("export to file") {
            {
                setPageComplete(false);
            }

            @Override
            public void createControl(Composite parent) {
                Text control = new Text(parent, SWT.BORDER);
                control.addModifyListener(new ModifyListener() {

                    @Override
                    public void modifyText(ModifyEvent e) {
                        text = ((Text) e.getSource()).getText();
                        setPageComplete(!text.equals(""));
                    }
                });
                setControl(control);
            }

        });
    }

    @Override
    public boolean performFinish() {
        Biomarker b = Models.getInstance().getActiveBiomarker();

        return true;
    }
}
