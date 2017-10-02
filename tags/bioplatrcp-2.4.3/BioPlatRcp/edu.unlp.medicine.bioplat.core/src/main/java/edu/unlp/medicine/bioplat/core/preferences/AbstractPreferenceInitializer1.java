package edu.unlp.medicine.bioplat.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import edu.unlp.medicine.bioplat.core.Activator;

public class AbstractPreferenceInitializer1 extends AbstractPreferenceInitializer {

	public AbstractPreferenceInitializer1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences prefs = DefaultScope.INSTANCE.getNode(Activator.id());
		prefs.put(AuthorPreferencePage.AUTHOR, "bioplat");
	}

}
