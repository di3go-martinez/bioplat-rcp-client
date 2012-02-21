package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPropertyListener;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.AbstractFormEditor;
import edu.unlp.medicine.bioplat.rcp.editor.EditorDescription;
import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.arnmPlatform.ARNmPlatform;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

public class ExperimentEditor extends AbstractFormEditor<Experiment> {

	public static String id() {
		return "bio.plat.experiment.editor";
	}

	public ExperimentEditor() {

	}

	private ExperimentEditor0 innerEditor;

	@Override
	protected List<EditorDescription> createEditors() {

		final Experiment experiment = model();

		List<EditorDescription> result = Lists.newArrayList();

		innerEditor = new ExperimentEditor0();
		EditorDescription ed = new EditorDescription(getEditorInput(), innerEditor);

		// sincronizo el nombre de la solapa con la primera solapa
		innerEditor.addPropertyListener(new IPropertyListener() {

			@Override
			public void propertyChanged(Object source, int propId) {
				setPartName(innerEditor.getPartName());
			}
		});

		result.add(ed);

		// Agrego la clinical data
		result.add(new EditorDescription(getEditorInput(), new ExperimentClinicalData()));

		// Agrego la plataforma
		ARNmPlatform p = experiment.getArnmPlatform();
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
		// result.add(new
		// EditorDescription(EditorInputFactory.createDefaultEditorInput(Persona.random()),
		// new PersonaEditor()));
		return result;
	}

	public void showGene(Gene selectedGene) {
		innerEditor.showGene(selectedGene);

	}
}
