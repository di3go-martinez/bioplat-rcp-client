package edu.unlp.medicine.bioplat.rcp.ui.biomarker.contributor;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.blindSearch.BLindSearchWizard;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.PSOWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * Abre el wizard que dispara el PSO
 * 
 * @author diego matínez
 * 
 */
public class BlindSearchOptimzerActionContribution extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				Biomarker b = model();
				BLindSearchWizard wizard = new BLindSearchWizard(b);
						wizard.blockOnOpen().open();
			}
		});
	}

}
