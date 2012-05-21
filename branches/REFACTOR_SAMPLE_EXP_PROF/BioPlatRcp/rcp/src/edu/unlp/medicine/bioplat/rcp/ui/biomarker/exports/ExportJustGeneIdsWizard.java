package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.util.Map;

import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.ExportJustGeneIdsCommand;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class ExportJustGeneIdsWizard extends ExportToFileWizard {

	@Override
	protected void doFinish(Biomarker b, Map<String, String> parameters) {
		new ExportJustGeneIdsCommand(b, parameters).execute();
	}
}
