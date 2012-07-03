package edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import edu.unlp.medicine.bioplat.rcp.application.Activator;

/**
 * 
 * Inicializa la configuración básica para poder utilizar una página de
 * preferencias en la aplicación
 * 
 * @author diego martínez
 * 
 */
public abstract class CustomFieldEditorPreferencePage extends FieldEditorPreferencePage {

	private ScopedPreferenceStore prefs;

	public CustomFieldEditorPreferencePage() {
		prefs = new ScopedPreferenceStore(ConfigurationScope.INSTANCE, Activator.id());
		setPreferenceStore(prefs);
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
