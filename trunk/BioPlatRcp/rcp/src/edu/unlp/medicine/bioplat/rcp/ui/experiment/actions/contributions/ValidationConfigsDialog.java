package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.dialogs.Dialog;
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
import com.google.common.collect.Lists;

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

public class ValidationConfigsDialog extends Dialog {

	protected ValidationConfigsDialog(Shell parentShell, Biomarker biomarker) {
		super(parentShell);
		this.model = biomarker;

		// TODO mejorar
		experimentsWizard = createExperimentSelectorWizard();
		// TODO conseguir la selección actual
		experimentsWizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);

	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 400);
	}

	private final List<ValidationConfig> data = Lists.newArrayList();

	@Override
	protected Control createContents(Composite parent) {
		Composite c = Widgets.createDefaultContainer(parent);

		tr = TableBuilder.create(c)//
				.addColumn(ColumnBuilder.create().property("experimentToValidate.name").title("experiment"))//
				.addColumn(ColumnBuilder.create().property("numberOfClusters").title("clusters"))//
				.addColumn(ColumnBuilder.create().property("statisticsSignificanceTest.friendlyName").title("Statistics Significance Test"))//
				.input(data).build();

		Button add = new Button(c, SWT.FLAT);
		add.setText("Agregar aplicación ");
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wd = new WizardDialog(PlatformUIUtils.findShell(), experimentsWizard);
				wd.setPageSize(400, 400);
				wd.setTitle("Agregar Validation Configs");
				wd.open();
			}
		});

		Button b = new Button(c, SWT.FLAT);
		b.setText("Apply");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				// TODO mejorar...
				for (ApplyExperimentsOnMetasignatureCommand command : applyCommands) {
					try {
						command.execute();
						MessageManager.INSTANCE.add(Message.info("Comando aplicado exitósamente"));
					} catch (Exception e) {
						MessageManager.INSTANCE.add(Message.error("Falló la aplicación del experimento", e));
					}
				}

			}
		});

		return c;
	}

	private Biomarker model;
	private AbstractWizard<?> experimentsWizard;
	private WizardDialog wd;
	private TableReference tr;

	private Biomarker findModel() {
		return model;
	}

	private List<ApplyExperimentsOnMetasignatureCommand> applyCommands = Lists.newArrayList();

	private AbstractWizard<?> createExperimentSelectorWizard() {
		final AbstractWizard<List<AbstractExperimentDescriptor>> w = new AbstractWizard<List<AbstractExperimentDescriptor>>() {

			@Override
			public List<AbstractExperimentDescriptor> backgroundProcess(Monitor feedback) throws ExperimentBuildingException {
				List<Experiment> experiments = ((List<Experiment>) model().get(PagesDescriptors.SELECTED));

				// TODO hacer para los otros sources (archivo, InSilicoDB,
				// etc)
				return Lists.transform(experiments, new Function<Experiment, AbstractExperimentDescriptor>() {

					@Override
					public AbstractExperimentDescriptor apply(Experiment input) {
						return new FromMemoryExperimentDescriptor(input);
					}
				});
			}

			@Override
			protected void doInUI(List<AbstractExperimentDescriptor> appliedExperiments) throws Exception {

				boolean shouldGenerateCluster = model().value(PagesDescriptors.GENERATE_CLUSTER_CALCULATE_BIOLOGICAL_VALUE);
				int numberOfClusters = model().value(PagesDescriptors.NUMBER_OF_CLUSTERS);
				String attributeNameToValidation = model().value(PagesDescriptors.ATTRIBUTE_NAME_TO_VALIDATION);
				String secondAttributeNameToDoTheValidation = model().value(PagesDescriptors.SECOND_ATTRIBUTE_NAME_TO_VALIDATION);
				IStatisticsSignificanceTest statisticsSignificanceTest = model().value(PagesDescriptors.STATISTICAL_TEST_VALUE);
				int numberOfTimesToRepeatTheCluster = model().value(PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING);
				boolean removeInBiomarkerTheGenesThatAreNotInTheExperiment = model().value(PagesDescriptors.REMOVE_GENES_IN_BIOMARKER);

				for (AbstractExperimentDescriptor aed : appliedExperiments) {

					final ValidationConfig validationConfig = new ValidationConfig(aed, shouldGenerateCluster, numberOfClusters, attributeNameToValidation, secondAttributeNameToDoTheValidation, statisticsSignificanceTest, numberOfTimesToRepeatTheCluster, removeInBiomarkerTheGenesThatAreNotInTheExperiment);

					applyCommands.add(new ApplyExperimentsOnMetasignatureCommand(findModel(), Arrays.asList(validationConfig)));

					// FIXME hacer un poquito más generico con una
					// interface MultipageEditor#addPage(Editor, Input,
					// [title])...
					// MultiPageBiomarkerEditor multipleEditor =
					// (MultiPageBiomarkerEditor)
					// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					//
					// final ExperimentAppliedToAMetasignature appliedExperiment
					// = findModel().getExperimentApplied(ae);
					// multipleEditor.addEditorPage(new ExperimentEditor(),
					// EditorInputFactory.createDefaultEditorInput(appliedExperiment));

					// old... PlatformUIUtils.openEditor(appliedExperiment,
					// ExperimentEditor.id());

					// TODO sacar, esto es provisorio
					data.add(validationConfig);
					tr.refresh();
				}

			}

			@Override
			protected WizardModel createWizardModel() {
				return new WizardModel()//
						.add(PagesDescriptors.GENERATE_CLUSTER_CALCULATE_BIOLOGICAL_VALUE, new WritableValue(true, Boolean.class))//
						.add(PagesDescriptors.NUMBER_OF_CLUSTERS, new WritableValue(2, Integer.class))//
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
