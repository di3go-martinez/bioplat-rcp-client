package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

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
					FileDialog dialog = new FileDialog(PlatformUIUtils.findShell(), SWT.SAVE);
					dialog.setFileName(model().getName()+".pdf");
					
					Map<String, String> properties = new HashMap<String, String>();
					properties.put("targetFile", dialog.open());
					GeneratePDFFromBiomarkerCommand exportToPDF = new GeneratePDFFromBiomarkerCommand(model(), properties);
					exportToPDF.execute();
					
				}
			});
			
		}
		
		
		
	
}
