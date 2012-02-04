package edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import edu.unlp.medicine.bioplat.rcp.application.Activator;

public class ExperimentGeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String EXPERIMENT_GRID_MAX_GENES = "EXPERIMENT_GRID_MAX_GENES";

	public static final String EXPERIMENT_GRID_MAX_SAMPLES = "EXPERIMENT_GRID_MAX_SAMPLES";

	private ScopedPreferenceStore prefs;

	public ExperimentGeneralPreferencePage() {
		prefs = new ScopedPreferenceStore(ConfigurationScope.INSTANCE, Activator.id());
		setPreferenceStore(prefs);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		// addField(new IntegerFieldEditor(EXPERIMENT_GRID_MAX_SAMPLES,
		// "máximo de samples", getFieldEditorParent()));
		addField(new IntegerFieldEditor(EXPERIMENT_GRID_MAX_GENES, "Máximo de genes en la grilla de experimentos", getFieldEditorParent()));
	}

	@Override
	public boolean performOk() {
		try {
			prefs.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.performOk();
	}
}