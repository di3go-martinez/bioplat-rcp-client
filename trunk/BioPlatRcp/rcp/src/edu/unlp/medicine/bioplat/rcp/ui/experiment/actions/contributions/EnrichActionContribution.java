package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class EnrichActionContribution extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				if ("true".equals(System.getProperty("INTERNAL_ENRICHR_BROWSER", "false")))
					// por ahora no anda...
					new EnrichrDialog().openWith(model());
				else
					new ExternalEnrichR().openWith(model());
			}
		});
	}
}
