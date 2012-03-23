package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Function;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Lists;
import com.google.common.collect.Ranges;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.ApplyExperimentsOnMetasignatureCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.AbstractExperimentDescriptor;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.FromMemoryExperimentDescriptor;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.AttributeTypeEnum;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.IStatisticsSignificanceTest;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.ValidationConfig;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;
import edu.unlp.medicine.utils.monitor.Monitor;

public class ValidationConfigsDialog extends TitleAreaDialog {

	protected ValidationConfigsDialog(Shell parentShell, Biomarker biomarker) {
		super(parentShell);
		this.biomarker = biomarker;

		// TODO mejorar
		experimentsWizard = createExperimentSelectorWizard();
		// TODO conseguir la selección actual
		experimentsWizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);

	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 500);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Configurar y aplicar experimentos");
	}

	private final List<ValidationConfig> data = Lists.newArrayList();

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		Composite c = Widgets.createDefaultContainer(container);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());

		tr = TableBuilder.create(c)//
				.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("experimentToValidate.name").title("experiment"))//
				.addColumn(ColumnBuilder.create().property("shouldGenerateCluster").checkbox().centered().width(20).title("cluster?"))//
				.addColumn(ColumnBuilder.create().property("numberOfClusters").title("clusters"))//
				.addColumn(ColumnBuilder.create().property("numberOfTimesToRepeatTheCluster").title("times"))//
				.addColumn(ColumnBuilder.create().property("statisticsSignificanceTest.friendlyName").title("Statistics Significance Test"))//
				.addColumn(ColumnBuilder.create().property("attribtueNameToDoTheValidation"))//
				.addColumn(ColumnBuilder.create().property("secondAttribtueNameToDoTheValidation"))//

				.input(data).build();

		// Composite buttons = new Composite(c, SWT.BORDER);
		// buttons.setLayout(GridLayoutFactory.fillDefaults().create());
		// buttons.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// true).create());

		setMessage("Configuración de experimentos a aplicar");
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		Composite c = new Composite(parent, SWT.None);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());
		Button add = new Button(c, SWT.NONE);
		// add.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// true).align(SWT.END, SWT.END).create());
		add.setText("+");
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wd = new WizardDialog(PlatformUIUtils.findShell(), experimentsWizard);
				wd.setPageSize(400, 400);
				wd.setTitle("Agregar Validation Configs");
				wd.open();
			}
		});
		super.createButtonsForButtonBar(c);

	}

	@Override
	protected void okPressed() {
		int count = 0;
		for (ApplyExperimentsOnMetasignatureCommand command : applyCommands) {
			try {
				command.execute();
				count++;
			} catch (Exception e) {
				MessageManager.INSTANCE.add(Message.error("Falló la aplicación del experimento", e));
			}
		}
		String s = (count == 1) ? "" : "s";
		MessageManager.INSTANCE.add(Message.info(count + " comando" + s + " aplicado" + s + " exitósamente"));
		close();
	}

	private Biomarker biomarker;
	private AbstractWizard<?> experimentsWizard;
	private WizardDialog wd;
	private TableReference tr;

	private Biomarker findBiomarker() {
		return biomarker;
	}

	private List<ApplyExperimentsOnMetasignatureCommand> applyCommands = Lists.newArrayList();

	private AbstractWizard<?> createExperimentSelectorWizard() {
		final AbstractWizard<List<AbstractExperimentDescriptor>> w = new AbstractWizard<List<AbstractExperimentDescriptor>>() {

			@Override
			public List<AbstractExperimentDescriptor> backgroundProcess(Monitor feedback) throws ExperimentBuildingException {
				List<Experiment> experiments = ((List<Experiment>) wizardModel().value(PagesDescriptors.SELECTED));

				return Lists.transform(experiments, new Function<Experiment, AbstractExperimentDescriptor>() {

					@Override
					public AbstractExperimentDescriptor apply(Experiment input) {
						return new FromMemoryExperimentDescriptor(input);
					}
				});
				// TODO hacer para los otros sources (archivo, InSilicoDB, etc)
			}

			@Override
			protected void doInUI(List<AbstractExperimentDescriptor> appliedExperiments) throws Exception {

				boolean shouldGenerateCluster = wizardModel().value(PagesDescriptors.GENERATE_CLUSTER_CALCULATE_BIOLOGICAL_VALUE);
				// numberOfCluster puede ser un rango
				String numberOfClusters = wizardModel().value(PagesDescriptors.NUMBER_OF_CLUSTERS);
				String attributeNameToValidation = wizardModel().value(PagesDescriptors.ATTRIBUTE_NAME_TO_VALIDATION);
				String secondAttributeNameToDoTheValidation = wizardModel().value(PagesDescriptors.SECOND_ATTRIBUTE_NAME_TO_VALIDATION);
				IStatisticsSignificanceTest statisticsSignificanceTest = wizardModel().value(PagesDescriptors.STATISTICAL_TEST_VALUE);
				int numberOfTimesToRepeatTheCluster = wizardModel().value(PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING);
				boolean removeInBiomarkerTheGenesThatAreNotInTheExperiment = wizardModel().value(PagesDescriptors.REMOVE_GENES_IN_BIOMARKER);

				for (AbstractExperimentDescriptor aed : appliedExperiments) {

					for (Integer clusters : calculateRange(numberOfClusters)) {
						final ValidationConfig validationConfig = new ValidationConfig(aed, shouldGenerateCluster, clusters, attributeNameToValidation, secondAttributeNameToDoTheValidation, statisticsSignificanceTest, numberOfTimesToRepeatTheCluster, removeInBiomarkerTheGenesThatAreNotInTheExperiment);

						applyCommands.add(new ApplyExperimentsOnMetasignatureCommand(findBiomarker(), Arrays.asList(validationConfig)));

						// FIXME hacer un poquito más generico con una
						// interface MultipageEditor#addPage(Editor, Input,
						// [title])...
						// MultiPageBiomarkerEditor multipleEditor =
						// (MultiPageBiomarkerEditor)
						// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
						//
						// final ExperimentAppliedToAMetasignature
						// appliedExperiment
						// = findModel().getExperimentApplied(ae);
						// multipleEditor.addEditorPage(new ExperimentEditor(),
						// EditorInputFactory.createDefaultEditorInput(appliedExperiment));

						data.add(validationConfig);
					}
					tr.refresh();
				}

			}

			/**
			 * 
			 * @param numberOfClusters
			 *            un string que representa un rango VÁLIDO (4..5) o un
			 *            número
			 * @return
			 * @see PagesDescriptors#configurationPage()
			 */
			private Set<Integer> calculateRange(String numberOfClusters) {
				// si es un número armo un rango de ese número hasta ese número,
				// que a es equivalente
				if (!numberOfClusters.contains(".."))
					numberOfClusters = numberOfClusters + ".." + numberOfClusters;
				String[] r = numberOfClusters.split("\\.\\.");
				return Ranges.closed(new Integer(r[0]), new Integer(r[1])).asSet(DiscreteDomains.integers());
			}

			@Override
			protected WizardModel createWizardModel() {
				return new WizardModel()//
						.add(PagesDescriptors.GENERATE_CLUSTER_CALCULATE_BIOLOGICAL_VALUE, new WritableValue(true, Boolean.class))//
						// el "número" de clusters puede ser un rango
						.add(PagesDescriptors.NUMBER_OF_CLUSTERS, new WritableValue("2..3", String.class))//
						.add(PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING, new WritableValue(10, Integer.class))//
						.add(PagesDescriptors.VALIDATION_TYPE)//
						.add(PagesDescriptors.ATTRIBUTE_TYPE, new WritableValue(null, AttributeTypeEnum.class))//
						.add(PagesDescriptors.STATISTICAL_TEST_VALUE, new WritableValue("", String.class))//
						.add(PagesDescriptors.ATTRIBUTE_NAME_TO_VALIDATION)//
						.add(PagesDescriptors.SECOND_ATTRIBUTE_NAME_TO_VALIDATION)//
						.add(PagesDescriptors.REMOVE_GENES_IN_BIOMARKER, new WritableValue(false, Boolean.class));
			}

			@Override
			protected List<WizardPageDescriptor> createPagesDescriptors() {
				return Arrays.asList(PagesDescriptors.experimentsWPD(), PagesDescriptors.configurationPage());
			}

			@Override
			protected String getTaskName() {
				return "Aplicando Experimentos";
			}
		};
		return w;
	}
}
