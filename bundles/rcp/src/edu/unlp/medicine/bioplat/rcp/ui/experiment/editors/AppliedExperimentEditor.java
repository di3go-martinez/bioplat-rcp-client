package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.editor.EditorDescription;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals.StatisticalDataEditor;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;

public class AppliedExperimentEditor extends ExperimentEditor {

	public static String id() {
		return "bio.plat.applied.experiment.editor";
	}

	@Override
	protected List<EditorDescription> createEditors() {
		List<EditorDescription> tabs = super.createEditors();
		tabs.add(0, new EditorDescription(getEditorInput(), new StatisticalDataEditor(false), "Statistical Data"));
		return tabs;
	}

	// TODO que no sea un método estático...
	public static void makeView(Composite container, Validation model) {
		StatisticalDataEditor.makeView(container, model);

	}
}
