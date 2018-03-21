package edu.unlp.medicine.bioplat.rcp.ui.experiment.exports;

import org.eclipse.jface.wizard.WizardDialog;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.Experiment;

/**
 * Abre el wizard que dispara el PSO
 * 
 * @author diego matínez
 * 
 */
public class ExportExperimentActionContribution extends AbstractActionContribution<Experiment> {

	@Override
	public void run() {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				Experiment e = model();
				ExportExperimentToFileWizard wizard = new ExportExperimentToFileWizard(e);
				wizard.init();
				WizardDialog dialog = new WizardDialog(PlatformUIUtils.findShell(), wizard);
				dialog.open(); 
				
				//wizard.blockOnOpen().open();
			}
		});
	}

}
