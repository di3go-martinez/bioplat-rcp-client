package edu.unlp.medicine.bioplat.rcp.ui.entities;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.MultiPageBiomarkerEditor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;

public class EditorsId {

	public static String biomarkerEditorId() {
		return MultiPageBiomarkerEditor.id();
	}

	public static String experimentEditorId() {
		return ExperimentEditor.id();
	}
}
