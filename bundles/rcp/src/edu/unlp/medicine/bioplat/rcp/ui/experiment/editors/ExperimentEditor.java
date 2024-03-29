package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.AbstractFormEditor;
import edu.unlp.medicine.bioplat.rcp.editor.EditorDescription;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.BiomarkerExperimentsEditor;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.BrowserEditorManager;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.LogRankTestExperimentExperimentsEditor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences.ExperimentGeneralPreferencePage;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.GeneViewPart;
import edu.unlp.medicine.bioplat.rcp.ui.utils.preferences.ExternalURLInformationPage;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.arnmPlatform.ARNmPlatform;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Experiment;
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
		EditorDescription ed = new EditorDescription(getEditorInput(), innerEditor, "Expression data");

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

					Widgets.createTextWithLabel(container, "Name", model(), "name");
				}
			}, "Platform");

			result.add(ed);

		}
		
		
		// david: Estos quedan deprecados
		//result.add(new EditorDescription(getEditorInput(), new SurvCompExperimentExperimentsEditor(false), "Concordance index validations"));
		//result.add(new EditorDescription(getEditorInput(), new LogRankTestExperimentExperimentsEditor(false), "Kaplan-Meier and Log-Rank validations"));
		
		// Genero un biomarker 
		((Experiment)model()).getValidationManager().setBiomarker(Biomarker.getFakeBiomarker());
		final IEditorInput input = EditorInputFactory.createDefaultEditorInput(((Experiment)model()).getValidationManager().getBiomarker());
		result.add(new EditorDescription(input, new BiomarkerExperimentsEditor(false), "Statistic Analysis"));
		

		new BrowserEditorManager(ExternalURLInformationPage.APPLY_TO_EXPERIMENT) {
			@Override
			protected String getAllGenesAsList() {
				List<Gene> genes = model().getGenes();
				StringBuilder sb = new StringBuilder();
				for (Gene gene : genes) {
					sb.append(gene.getEntrezId()).append(",");
				}
				return sb.toString();
			}

			@Override
			protected String getAllGenesAsListWithPipes() {
				List<Gene> genes = model().getGenes();
				StringBuilder sb = new StringBuilder();
				for (Gene gene : genes) {
					sb.append(gene.getEntrezId()).append("|");
				}
				return sb.toString();
			}
		}.createEditorBrowsers(result, getEditorInput());

		try {
			GeneViewPart view = (GeneViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(GeneViewPart.id());
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
		return preferences().getInt(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_MAX_SAMPLES, 100);
	}

	// TODO sacar de acá
	private static IEclipsePreferences ep;

	static IEclipsePreferences preferences() {
		if (ep == null)
			ep = PlatformUtils.preferences();
		return ep;
	}

	/**
	 * 
	 * Se chequea que las columnas de samples que está mostrando la tr
	 * corresponden al model, sino se sacan.
	 * 
	 * 
	 * @param tr
	 *            TableRef
	 * @param model
	 *            experimento
	 */
	// TODO ¿implementar el agregado?
	static <T extends AbstractExperiment> void checkColumns(TableReference tr, T model) {
		for (TableColumn tc : tr.getTable().getColumns()) {
			String columnName = tc.getText();
			if (columnName.startsWith("GSM") && !model.getSampleNames().contains(columnName)) {
				tc.setWidth(0);
				tc.setResizable(false);
				// reloadModel = true;
			}
		}

	}
}
