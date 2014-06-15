package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import org.eclipse.swt.widgets.FileDialog;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.save.MetaSignatureMarshaller;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class ExportToBPLActionContribution extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {

				FileDialog dialog = new FileDialog(PlatformUIUtils.findShell());
				dialog.setFilterNames(new String[] { "BPL Files", "All files" });
				dialog.setFilterExtensions(new String[] { "*.bpl", "*.*" }); // Windows
																				// //
																				// wild

				String filename = dialog.open();
				MetaSignatureMarshaller marshaller = new MetaSignatureMarshaller();

				if (filename != null) {
					marshaller.marshal(model(), check(filename, dialog));
				}

			}

			/**
			 * Chequea la configuración del dialog contra el nombre de archivo
			 * elegido. ejemplo comleta la extensión si la tiene
			 * 
			 * @param filename
			 * @param dialog
			 * @return
			 */
			protected String check(String filename, FileDialog dialog) {
				int extensioni = dialog.getFilterIndex();
				String extension = "";
				if (extensioni != -1) {
					extension = dialog.getFilterExtensions()[extensioni];
					if (!extension.equals("*.*"))
						extension = extension.replace("*", "");
				}
				if (!filename.endsWith(extension) && !extension.equals("*.*"))
					filename = filename + extension;

				return filename;
			}
		});
	}

}
