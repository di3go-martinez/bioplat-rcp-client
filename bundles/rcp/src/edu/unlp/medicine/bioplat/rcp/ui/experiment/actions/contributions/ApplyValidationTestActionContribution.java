/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import static edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils.findShell;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * @author Juan
 *
 */
public class ApplyValidationTestActionContribution extends
		AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				new ValidationTestDialog(findShell(), model()).open();

			}
		});

	}
}
