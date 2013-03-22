package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.editor.EditorDescription;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.statisticals.StatisticalDataEditor;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;

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

	public static void makeView(Composite container, ExperimentAppliedToAMetasignature model) {
		StatisticalDataEditor.makeView(container, model);

	}
}
