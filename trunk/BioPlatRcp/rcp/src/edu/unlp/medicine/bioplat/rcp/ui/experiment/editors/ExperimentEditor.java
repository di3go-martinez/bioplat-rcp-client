package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPropertyListener;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.AbstractFormEditor;
import edu.unlp.medicine.bioplat.rcp.editor.EditorDescription;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences.ExperimentGeneralPreferencePage;
import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.arnmPlatform.ARNmPlatform;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.gene.Gene;

public class ExperimentEditor extends AbstractFormEditor<AbstractExperiment> {

	public static String id() {
		return "bio.plat.experiment.editor";
	}

	public ExperimentEditor() {

	}

	private ExperimentEditor0 innerEditor;

	@Override
	protected List<EditorDescription> createEditors() {

		final AbstractExperiment experiment = model();
		List<EditorDescription> result = Lists.newArrayList();

		innerEditor = new ExperimentEditor0();
		// TODO revisar el title, por qué lo pone vacío al principio sino le
		// mando el model.id?...
		EditorDescription ed = new EditorDescription(getEditorInput(), innerEditor, getEditorInput().model().id());

		// sincronizo el nombre del editor con la primera solapa
		innerEditor.addPropertyListener(new IPropertyListener() {

			@Override
			public void propertyChanged(Object source, int propId) {
				setPartName(innerEditor.getPartName());
			}
		});

		result.add(ed);

		result.add(new EditorDescription(getEditorInput(), new ExperimentClinicalData(false), "Clinical Data"));

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

	// ???
	public void showGene(Gene selectedGene) {
		innerEditor.showGene(selectedGene);
	}

	/**
	 * 
	 * La cantidad de samples a cargar en la vista se configuran desde las
	 * preferencias de la aplicación
	 * 
	 * @return la cantidad de samples a cargar en la vista
	 */
	static int getSampleCountToLoad() {
		return ep().getInt(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_MAX_SAMPLES, 20);
	}

	// TODO sacar de acá
	private static IEclipsePreferences ep;

	static IEclipsePreferences ep() {
		if (ep == null)
			ep = PlatformUtils.preferences();
		return ep;
	}

}
