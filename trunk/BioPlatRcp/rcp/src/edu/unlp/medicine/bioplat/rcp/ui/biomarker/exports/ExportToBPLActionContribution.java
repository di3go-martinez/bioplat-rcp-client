package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.save.MetaSignatureMarshaller;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;
import edu.unlp.medicine.entity.experiment.Experiment;


public class ExportToBPLActionContribution extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				
				
				FileDialog dialog = new FileDialog(PlatformUIUtils.findShell());
				dialog.setFilterNames(new String[] { "BPL Files" });
				dialog.setFilterExtensions(new String[] { "*.bpl" }); // Windows // wild

				String filename = dialog.open();
				MetaSignatureMarshaller marshaller = new MetaSignatureMarshaller();
				if (filename != null)
					marshaller.marshal(model(), filename);

				
			}
		});
	}

}
