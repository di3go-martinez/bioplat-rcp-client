package edu.unlp.medicine.bioplat.rcp.ui.utils.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;

/**
 * 
 * Página de configuración de urls externas para biomarcadores y experimentos
 * 
 * @author diego martínez
 * 
 */
public class ExternalURLInformationPage extends CustomFieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String APPLY_TO_GENE_SIGNATURE = "ApplyToGeneSignature";
	public static final String APPLY_TO_EXPERIMENT = "ApplyToExperiment";
	public static final String EXTERNAL_URLS = "URLS";
	public static final String SEPARATOR = "|";

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		final Composite composite = getFieldEditorParent();

		Composite container = Widgets.createDefaultContainer(composite, 2);
		addField(new GenesUrlEditor(EXTERNAL_URLS, "Urls", container, "{allGenes}"));
		addField(new BooleanFieldEditor(APPLY_TO_EXPERIMENT, "Apply to Experiments", composite));
		addField(new BooleanFieldEditor(APPLY_TO_GENE_SIGNATURE, "Apply to Gene Signatures", composite));
	}
}
