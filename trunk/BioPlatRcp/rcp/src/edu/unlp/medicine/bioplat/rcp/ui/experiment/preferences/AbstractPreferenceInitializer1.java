package edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import edu.unlp.medicine.bioplat.rcp.application.Activator;

public class AbstractPreferenceInitializer1 extends AbstractPreferenceInitializer {

	public AbstractPreferenceInitializer1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences prefs = DefaultScope.INSTANCE.getNode(Activator.id());
		prefs.putInt(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_MAX_SAMPLES, 10);
		prefs.putInt(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_MAX_GENES, 1000);
		prefs.putBoolean(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_AUTO_REFRESH, true);
	}

}
