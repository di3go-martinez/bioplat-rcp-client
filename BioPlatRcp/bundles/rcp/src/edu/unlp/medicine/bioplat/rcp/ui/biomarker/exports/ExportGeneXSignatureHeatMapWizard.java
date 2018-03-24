package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.util.Map;

import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.ExportGeneXSignatureHeatMap;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class ExportGeneXSignatureHeatMapWizard extends ExportToFileWizard {

	@Override
	protected void doFinish(Biomarker b, Map<String, String> parameters) {
		new ExportGeneXSignatureHeatMap(b, parameters).execute();
	}
}
