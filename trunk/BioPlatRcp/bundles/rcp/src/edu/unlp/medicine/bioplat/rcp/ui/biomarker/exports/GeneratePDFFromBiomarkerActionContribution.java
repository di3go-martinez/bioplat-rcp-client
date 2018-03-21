package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import org.eclipse.jface.wizard.WizardDialog;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class GeneratePDFFromBiomarkerActionContribution extends AbstractActionContribution<Biomarker> {
			
			
		@Override
		public void run() {
			inDisplay(new Runnable() {
				@Override
				public void run() {
					
					Biomarker b = model();
					GeneratePDFFromBiomarkerWizard wizard = new GeneratePDFFromBiomarkerWizard(b);
					wizard.init();
					WizardDialog dialog = new WizardDialog(PlatformUIUtils.findShell(), wizard);
					dialog.open();
					
				}
			});
			
		}
		
		
		
	
}
