package edu.unlp.medicine.bioplat.core.preferences;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.preferences.CustomFieldEditorPreferencePage;
import edu.unlp.medicine.domainLogic.framework.classifiers.ListClassifiers;

public class AuthorPreferencePage extends CustomFieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String AUTHOR = "AUTHOR";

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(AUTHOR, "Author", getFieldEditorParent()));
	}

	public static  String author() {
		IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode(Activator.id());
		return prefs.get(AUTHOR, ListClassifiers.DEFAULT_USER);
	}

}
