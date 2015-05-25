package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.util.HashMap;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.GeneratePDFFromBiomarkerCommand;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class GeneratePDFFromBiomarkerActionContribution extends AbstractActionContribution<Biomarker> {
			
			
		@Override
		public void run() {
			PlatformUIUtils.findDisplay().asyncExec(new Runnable() {
	
				@Override
				public void run() {
					
					GeneratePDFFromBiomarkerCommand exportToPDF = new GeneratePDFFromBiomarkerCommand(model(), new HashMap<String, String>());
					exportToPDF.execute();
				}
			});
			
		}
		
		
		
	
}
