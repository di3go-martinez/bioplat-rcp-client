package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.AbstractFormEditor;
import edu.unlp.medicine.bioplat.rcp.editor.EditorDescription;
import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.arnmPlatform.ARNmPlatform;
import edu.unlp.medicine.entity.experiment.Experiment;

public class ExperimentEditor extends AbstractFormEditor<Experiment> {

	public static String id() {
		return "bio.plat.experiment.editor";
	}

	public ExperimentEditor() {

	}

	@Override
	protected List<EditorDescription> createEditors() {

		List<EditorDescription> result = Lists.newArrayList();

		EditorDescription ed = new EditorDescription(getEditorInput(), new ExperimentEditor0(), "Experimento");

		result.add(ed);

		Experiment experiment = model();
		ARNmPlatform p = experiment.getArnMlatform();
		// TODO definir...
		if (p != null) { // si hay plataforma creo la solapa correspondiente
			IEditorInput i = EditorInputFactory.createDefaultEditorInput(p);
			ed = new EditorDescription(i, new AbstractEditorPart<ARNmPlatform>() {

				@Override
				protected void doCreatePartControl(Composite parent) {
					Composite container = Widgets.createDefaultContainer(parent);

					Widgets.createTextWithLabel(container, "Nombre", model(), "name");
				}
			}, "Plataforma");

			result.add(ed);
		}

		// prueba
		//result.add(new EditorDescription(EditorInputFactory.createDefaultEditorInput(Persona.random()), new PersonaEditor()));
		return result;
	}

}
