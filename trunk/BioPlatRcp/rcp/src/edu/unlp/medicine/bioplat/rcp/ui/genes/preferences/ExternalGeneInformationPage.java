package edu.unlp.medicine.bioplat.rcp.ui.genes.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import edu.unlp.medicine.bioplat.rcp.application.Activator;

public class ExternalGeneInformationPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String URLS = "urls";
	private ScopedPreferenceStore prefs;

	public ExternalGeneInformationPage() {
		prefs = new ScopedPreferenceStore(ConfigurationScope.INSTANCE, Activator.id());
		setPreferenceStore(prefs);
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(URLS, "Ingresar urls separados por | (usar {genId})", getFieldEditorParent()));
	}

	@Override
	public boolean performOk() {
		try {
			prefs.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.performOk();
	}

}
