package edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.unlp.medicine.bioplat.rcp.ui.utils.preferences.CustomFieldEditorPreferencePage;

public class ExperimentGeneralPreferencePage extends CustomFieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String EXPERIMENT_GRID_MAX_GENES = "EXPERIMENT_GRID_MAX_GENES";
	public static final String EXPERIMENT_GRID_MAX_SAMPLES = "EXPERIMENT_GRID_MAX_SAMPLES";
	public static final String EXPERIMENT_GRID_AUTO_REFRESH = "EXPERIMENT_GRID_AUTO_REFRESH";

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		addField(new IntegerFieldEditor(EXPERIMENT_GRID_MAX_SAMPLES, "Max samples on grid", getFieldEditorParent()));
		addField(new IntegerFieldEditor(EXPERIMENT_GRID_MAX_GENES, "Max genes on grid", getFieldEditorParent()));
		addField(new BooleanFieldEditor(EXPERIMENT_GRID_AUTO_REFRESH, "Grid refreshing on modificaition (experimental)", getFieldEditorParent()));
	}

}
