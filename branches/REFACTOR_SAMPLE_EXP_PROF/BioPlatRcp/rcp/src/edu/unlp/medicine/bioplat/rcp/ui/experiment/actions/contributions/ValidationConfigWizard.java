package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.observable.value.WritableValue;

import com.google.common.base.Function;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Lists;
import com.google.common.collect.Ranges;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
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

public class ValidationConfigWizard extends AbstractWizard<List<AbstractExperimentDescriptor>> {

	private Biomarker biomarker;

	public ValidationConfigWizard(Biomarker biomarker) {
		this.biomarker = biomarker;
	}

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

	private List<ApplyExperimentsOnMetasignatureCommand> commands2apply = Lists.newArrayList();

	public List<ApplyExperimentsOnMetasignatureCommand> commands2apply() {
		return commands2apply;
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

				commands2apply.add(new ApplyExperimentsOnMetasignatureCommand(findBiomarker(), Lists.newArrayList(validationConfig)));

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

				register(validationConfig);
			}

		}
		afterExecution();
	}

	// TODO mejorar el nombre
	protected void afterExecution() {

	}

	// TODO mejorar el nombre
	protected void register(ValidationConfig validationConfig) {

	}

	private Biomarker findBiomarker() {
		return biomarker;
	}

	/**
	 * 
	 * @param numberOfClusters
	 *            un string que representa un rango VÁLIDO (4..5) o un número
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

}
