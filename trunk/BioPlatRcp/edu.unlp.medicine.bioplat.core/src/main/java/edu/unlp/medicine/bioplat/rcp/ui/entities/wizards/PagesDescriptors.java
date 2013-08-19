package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;

public class PagesDescriptors {

	public static final String OS_EVENT = "OS_Event";
	public static final String RFS_EVENT = "RFS_Event";

	public static final String OS_MONTHS = "OS_Months";
	public static final String RFS_MONTHS = "RFS_Months";

	// clave para el wizardModel que indica la lista de elementos seleccionados.
	// Es un common value, no un IObservableValue...
	public static final String SELECTED = "SELECTED";

	public static final String ATTRIBUTE_TYPE = "ATTRIBUTE_TYPE";
	public static final String GENERATE_CLUSTER_CALCULATE_BIOLOGICAL_VALUE = "GENERATE_CLUSTER_&_CALCULATE_BIOLOGICAL_VALUE";
	public static final String NUMBER_OF_CLUSTERS = "NUMBER_OF_CLUSTERS";
	public static final String TIMES_TO_REPEAT_CLUSTERING = "TIMES_TO_REPEAT_CLUSTERING";
	public static final String VALIDATION_TYPE = "VALIDATION_TYPE";
	public static final String STATISTICAL_TEST_VALUE = "STATISTICAL TEST VALUE";
	public static final String ATTRIBUTE_NAME_TO_VALIDATION = "ATTRIBUTE_NAME_TO_VALIDATION";
	public static final String SECOND_ATTRIBUTE_NAME_TO_VALIDATION = "SECOND_" + ATTRIBUTE_NAME_TO_VALIDATION;
	public static final String REMOVE_GENES_IN_GENE_SIGNATURE = "REMOVE_GENES_IN_BIOMARKER";
	public static final String OTHER_SECOND_ATTRIBUTE_NAME_TO_VALIDATION = "OTHER_SECOND_ATTRIBUTE_NAME_TO_VALIDATION";
	public static final String OTHER_ATTRIBUTE_NAME_TO_VALIDATION = "OTHER_ATTRIBUTE_NAME_TO_VALIDATION";
	@Deprecated
	public static final String USE_EXISTING_CLUSTER = "USE_EXISTING_CLUSTER";

	// GUI
	public static String OTHER = "Other";

	private static WizardPageDescriptor clusterPageDescriptor() {
		return new WizardPageDescriptor("Cluster") {

			@Override
			public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				return new Composite(parent, SWT.NONE);
			}
		};
	}

	/**
	 * Define y mantiene la variable SELECTED para indicar la lista de
	 * experimentos seleccionados
	 * 
	 * @param validationTestGUIProvider
	 * 
	 * @return
	 */
	// TODO agregar desde distintas fuentes, inSilico e archivo
	public static WizardPageDescriptor experimentsWPD(ValidationTestGUIProvider validationTestGUIProvider) {
		return new WizardPageDescriptor("Experiments for applying " + validationTestGUIProvider.getName()) {

			@Override
			public Composite create(final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {

				wp.setDescription("Select the experiments for validating your biomarker.");

				List<AbstractExperiment> editors = PlatformUIUtils.openedEditors(AbstractExperiment.class);

				Composite container = new Composite(parent, SWT.BORDER);
				final TableReference tr = TableBuilder.create(container).input(editors).hideTableLines()//
						.addColumn(ColumnBuilder.create().title("Experiments in your Bioplat Desktop").width(500).property("name"))//
						.build();
				tr.addSelectionChangeListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						wmodel.set(SELECTED, tr.selectedElements());
						wp.setPageComplete(isPageComplete(wmodel));
					}
				});

				GridLayoutFactory.fillDefaults().margins(10, 10).applyTo(container);
				return container;
			}

			@Override
			public boolean isPageComplete(WizardModel model) {
				if (model == null)
					return false;
				List<?> l = model.value(SELECTED);
				if (l == null)
					return false;
				return !l.isEmpty();
			}
		};
	}

	public static WizardPageDescriptor configurationPage(final ValidationTestGUIProvider validationTestGUIProvider) {
		return configurationPage(validationTestGUIProvider, false);
	}

	private static WizardPageDescriptor configurationPage(final ValidationTestGUIProvider validationTestGUIProvider, boolean forManualClustering) {
		return new ConfigurationWizardPageDescriptor(validationTestGUIProvider, "Validation strategy configuration ( " + validationTestGUIProvider.getName() + ")", forManualClustering);
	}

	public static WizardPageDescriptor configurationPageForManualClustering(ValidationTestGUIProvider provider) {
		return configurationPage(provider, true);
	}
}
