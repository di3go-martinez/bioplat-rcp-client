package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.value.WritableValue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.ValidationTestGUIProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.AbstractExperimentDescriptor;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.FromMemoryExperimentDescriptor;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.utils.monitor.Monitor;

public abstract class ValidationConfigManualClusterWizard extends AbstractWizard<AbstractExperimentDescriptor> {

	private ValidationTestGUIProvider provider;
	private Experiment experiment;
	private List<OneBiomarkerCommand> commands2apply = Lists.newArrayList();

	public ValidationConfigManualClusterWizard(Experiment experiment, ValidationTestGUIProvider provider) {
		this.experiment = experiment;
		this.provider = provider;
		provider.declareVariablesInWizardModel(wizardModel());
		this.setWindowTitle("Validate experiment using " + provider.getName() + " and preassigned cluster");
		/*
		 * FIXME sacar el cálculo del número de clusters de acá!!... TODO
		 * refactorrrrr.
		 */
		wizardModel().add(PagesDescriptors.NUMBER_OF_CLUSTERS, Integer.class, new HashSet(experiment.getGroups().values()).size());
		
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		return Lists.newArrayList(PagesDescriptors.configurationPageForManualClustering(provider));
	}

	@Override
	protected String getTaskName() {
		return provider.getName() + " Validation";
	}

	@Override
	protected AbstractExperimentDescriptor backgroundProcess(Monitor monitor) throws Exception {
		return new FromMemoryExperimentDescriptor(experiment);
	}

	@Override
	protected void doInUI(AbstractExperimentDescriptor result) throws Exception {

		// String numberOfClusters =
		// wizardModel().value(PagesDescriptors.NUMBER_OF_CLUSTERS);

		// no va wizardModel().value(PagesDescriptors.NUMBER_OF_CLUSTERS);

		String attributeNameToValidation = wizardModel().value(PagesDescriptors.ATTRIBUTE_NAME_TO_VALIDATION);
		String secondAttributeNameToDoTheValidation = wizardModel().value(PagesDescriptors.SECOND_ATTRIBUTE_NAME_TO_VALIDATION);

		if (secondAttributeNameToDoTheValidation.equals(PagesDescriptors.OTHER)) {
			secondAttributeNameToDoTheValidation = wizardModel().value(PagesDescriptors.OTHER_SECOND_ATTRIBUTE_NAME_TO_VALIDATION);
		}

		if (attributeNameToValidation.equals(PagesDescriptors.OTHER)) {
			attributeNameToValidation = wizardModel().value(PagesDescriptors.OTHER_ATTRIBUTE_NAME_TO_VALIDATION);
		}

		// IStatisticsSignificanceTest statisticsSignificanceTest =
		// wizardModel().value(PagesDescriptors.STATISTICAL_TEST_VALUE);
		// int numberOfTimesToRepeatTheCluster =
		// wizardModel().value(PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING);
		// boolean removeInBiomarkerTheGenesThatAreNotInTheExperiment =
		// wizardModel().value(PagesDescriptors.REMOVE_GENES_IN_GENE_SIGNATURE);

		// It adds additional parameters for the validation. It will delegate on
		// the validationTestGUIProvider
		Map<String, String> specificParametersForTheValidationTest = Maps.newHashMap();
		provider.getSpecificParametersForTheValidationTest(specificParametersForTheValidationTest, wizardModel());

		// for (AbstractExperimentDescriptor aed : appliedExperiments) {
		//
		// for (Integer clusters : calculateRange(numberOfClusters)) {

		final ValidationConfig4DoingCluster validationConfig = ValidationConfig4DoingCluster.withPrecalculatedCluster(result, attributeNameToValidation, secondAttributeNameToDoTheValidation);
		validationConfig.setAttribtueNameToDoTheValidation(attributeNameToValidation);
		validationConfig.setSecondAttribtueNameToDoTheValidation(secondAttributeNameToDoTheValidation);

		// new ValidationConfig4DoingCluster(aed, clusters,
		// attributeNameToValidation, secondAttributeNameToDoTheValidation,
		// numberOfTimesToRepeatTheCluster,
		// removeInBiomarkerTheGenesThatAreNotInTheExperiment);
		validationConfig.setSpecificParametersForTheValidationTest(specificParametersForTheValidationTest);

		// this.createCommand(findBiomarker(),
		// Lists.newArrayList(validationConfig)).execute();

		run(validationConfig);

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

		// register(validationConfig);
		// }
		//
		// }
		// afterExecution();

	}

	protected abstract void run(final ValidationConfig4DoingCluster validationConfig);

	protected abstract OneBiomarkerCommand createCommand(Biomarker biomarker, ArrayList<ValidationConfig4DoingCluster> list);

	@Override
	protected WizardModel createWizardModel() {
		WizardModel wm = super.createWizardModel();
		wm.set(PagesDescriptors.SELECTED, experiment);

		wm//
		.add(PagesDescriptors.GENERATE_CLUSTER_CALCULATE_BIOLOGICAL_VALUE, new WritableValue(true, Boolean.class))//

				// .add(PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING, new
				// WritableValue(10, Integer.class))//
				// .add(PagesDescriptors.VALIDATION_TYPE)//
				// .add(PagesDescriptors.ATTRIBUTE_TYPE, new WritableValue(null,
				// AttributeTypeEnum.class))//
				// .add(PagesDescriptors.STATISTICAL_TEST_VALUE, new
				// WritableValue("", String.class))//
				.add(PagesDescriptors.ATTRIBUTE_NAME_TO_VALIDATION)//
				.add(PagesDescriptors.SECOND_ATTRIBUTE_NAME_TO_VALIDATION)//
				.add(PagesDescriptors.OTHER_SECOND_ATTRIBUTE_NAME_TO_VALIDATION)//
				.add(PagesDescriptors.OTHER_ATTRIBUTE_NAME_TO_VALIDATION)//
				.add(PagesDescriptors.REMOVE_GENES_IN_GENE_SIGNATURE, new WritableValue(false, Boolean.class))//
		;

		WritableValue wv4RepeatCLuster = new WritableValue(1, Integer.class);
		wm.add(PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING, wv4RepeatCLuster);
		// wm.set(PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING, 1);

		return wm;
	}
}
