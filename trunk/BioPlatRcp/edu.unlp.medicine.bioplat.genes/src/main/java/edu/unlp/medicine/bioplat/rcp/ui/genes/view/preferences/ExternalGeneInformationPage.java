package edu.unlp.medicine.bioplat.rcp.ui.genes.view.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import edu.unlp.medicine.bioplat.rcp.ui.genes.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.preferences.GenesUrlEditor;

public class ExternalGeneInformationPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final String TEXT = "Urls \n\tYou can use the following variables to specify your URL :\n\t\t{genId}, {genName}, {ensemblId})\n\n\t\tFormat: ProviderName::Url";
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
		// addField(new StringFieldEditor(URLS,
		// "Ingresar urls separados por '|'\nSe pueden usar las siguientes variables:\n\t{genId}, {genName}, {ensemblId})\n",
		// getFieldEditorParent()));
		addField(new GenesUrlEditor(URLS, TEXT, getFieldEditorParent(), "{genId}, {genName}, {ensemblId}"));
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
