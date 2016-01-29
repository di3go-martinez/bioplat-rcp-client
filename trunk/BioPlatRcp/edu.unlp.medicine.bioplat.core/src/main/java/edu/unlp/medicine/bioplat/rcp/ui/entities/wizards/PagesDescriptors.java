package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
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
	//public static final String TIMES_TO_REPEAT_CLUSTERING = "TIMES_TO_REPEAT_CLUSTERING";
	public static final String VALIDATION_TYPE = "VALIDATION_TYPE";
	public static final String STATISTICAL_TEST_VALUE = "STATISTICAL TEST VALUE";
	public static final String ATTRIBUTE_NAME_TO_VALIDATION = "ATTRIBUTE_NAME_TO_VALIDATION";
	public static final String SECOND_ATTRIBUTE_NAME_TO_VALIDATION = "SECOND_" + ATTRIBUTE_NAME_TO_VALIDATION;
	public static final String REMOVE_GENES_IN_GENE_SIGNATURE = "REMOVE_GENES_IN_BIOMARKER";
	public static final String OTHER_SECOND_ATTRIBUTE_NAME_TO_VALIDATION = "OTHER_SECOND_ATTRIBUTE_NAME_TO_VALIDATION";
	public static final String OTHER_ATTRIBUTE_NAME_TO_VALIDATION = "OTHER_ATTRIBUTE_NAME_TO_VALIDATION";
	public static final String CLUSTERING_STRATEGY = "CLUSTERING_STRATEGY";
	public static final String MANUAL_CLUSTERING = "MANUAL_CLUSTERING"; 
	
	
	//Pam parameters
	public static final String PAM_METRIC = "PAM_METRIC";
	public static final String PAM_STANDARDIZED = "PAM_STANDARDIZED";
	
	// Kmeans parameters
	public static final String KMEANS_ALGORITHM = "KMEANS_ALGORITHM";
	public static final String KMEANS_ITER = "KMEANS_ITER";
	
	// Kmeans pàrameters
	public static final String KMEANSHCLUST_METRIC = "KMEANSHCLUST_ALGORITHM";
	
	
	
	
	
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

				wp.setDescription("Select the Bioplat experiments Bioplat for validating your Gene Signature. ");

				List<AbstractExperiment> editors = PlatformUIUtils.openedEditors(AbstractExperiment.class);

				Composite container = new Composite(parent, SWT.BORDER);
				container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());
				container.setLayoutData(GridDataFactory.fillDefaults().grab(true,true).create());
				
				
				Composite c = Widgets.createDefaultContainer(container);
				//c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());
				c.setLayoutData(GridDataFactory.fillDefaults().grab(true,true).create());
				
				String help = "Help: The experiments in the list are tohse imported to Bioplat. If you have a file with the expression and clinical data or you have a .CEL file you can easily import them using the start menu. You can also use public data importing experiments from CBioportal. \n\n";
				GUIUtils.addWrappedText(c, help, 8, true);

				
				
				
				final TableReference tr = TableBuilder.create(container).input(editors).hideTableLines()//
						.addColumn(ColumnBuilder.create().title("Experiments in your Bioplat Desktop").width(500).property("name"))//
						.build();

				GridLayoutFactory.fillDefaults().margins(10, 10).numColumns(1).generateLayout(c);
				tr.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true,true).create());

				
				//String help = "Help: Remember you have to import to Bioplat an experiment for using it for doing statistis analysis. You can use many of the available options for importing experiments. For acccess them go to start menu and then press GeneSingature/Experiments.\n\n";
				//GUIUtils.addWrappedText(parent, help, 8, true);
				

				
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
		WizardPageDescriptor cpw = configurationPage(validationTestGUIProvider, false, validationTestGUIProvider.getName() + " configuration, using cluster based on expression data");
		cpw.setDescription("Configure the 'Time' attribute and the 'Event' attribute for executing " + validationTestGUIProvider.getName() + ". The cluster will be calculated using the expression data of the experiment for the genes in the Gene Signature. So, you have to also configure the number of clusters you would like to use.");
		return cpw;
	}

	private static WizardPageDescriptor configurationPage(final ValidationTestGUIProvider validationTestGUIProvider, boolean forManualClustering, String title) {
		return new ConfigurationWizardPageDescriptor(validationTestGUIProvider, title, forManualClustering);
	}

	public static WizardPageDescriptor configurationPageForManualClustering(ValidationTestGUIProvider provider) {
		WizardPageDescriptor cpw = configurationPage(provider, true, provider.getName() + " configuration, using preassigned experiment clusters");
		cpw.setDescription("Configure the 'Time' attribute name and the 'Event' attribute name for executing " + provider.getName() + ". The algorithm will use the clusters you have defined in the experiment using the 'Configure cluster' operation");
		return cpw;
	}
}
