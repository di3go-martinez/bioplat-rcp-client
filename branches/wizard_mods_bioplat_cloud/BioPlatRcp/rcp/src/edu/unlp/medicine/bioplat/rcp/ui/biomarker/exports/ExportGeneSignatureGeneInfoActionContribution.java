package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import org.eclipse.jface.wizard.WizardDialog;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;
import edu.unlp.medicine.entity.experiment.Experiment;


public class ExportGeneSignatureGeneInfoActionContribution extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				
				Biomarker b = model();
				ExportJustGeneIdsWizard wizard = new ExportJustGeneIdsWizard(b);
				wizard.myInit();
				WizardDialog dialog = new WizardDialog(PlatformUIUtils.findShell(), wizard);
				dialog.open();
				
				
				//wizard.blockOnOpen().open();
			}
		});
	}

}
