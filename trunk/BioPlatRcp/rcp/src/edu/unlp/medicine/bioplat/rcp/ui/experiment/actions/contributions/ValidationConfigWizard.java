package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.ValidationTestGUIProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.AbstractExperimentDescriptor;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.FromMemoryExperimentDescriptor;
import edu.unlp.medicine.domainLogic.framework.statistics.clusterers.ClustererFactory;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.IClusterer;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;
import edu.unlp.medicine.utils.monitor.Monitor;

/**
 * Wizard de creación de ValidationConfigs para la aplicación de experimentos a
 * biomarcadores
 * 
 * @author diego martínez
 * 
 */
public abstract class ValidationConfigWizard extends AbstractWizard<List<AbstractExperimentDescriptor>> {

	// mutex para el job, operaciones K, S, que interactúan con R y se deben
	// ejecutar exclusivamente
	private static ISchedulingRule mutex = new ISchedulingRule() {

		@Override
		public boolean isConflicting(ISchedulingRule rule) {
			return this == rule;
		}

		@Override
		public boolean contains(ISchedulingRule rule) {
			return this == rule;
		}
	};

	/**
	 * <b>uso interno</b> <b>probablemente se mueva de lugar</b>
	 * 
	 * @return
	 */
	@Beta
	public static ISchedulingRule getMutexRule() {
		return mutex;
	}

	private Biomarker biomarker;
	private boolean acceptRange;
	private ValidationTestGUIProvider validationTestGUIProvider;

	@Override
	public int getWizardWidth() {
		return 700;
	}

	@Override
	public int getWizardHeight() {
		return 670;
	}

	/**
	 * 
	 * @param biomarker
	 * @param acceptRange
	 */
	public ValidationConfigWizard(Biomarker biomarker, boolean acceptRange, ValidationTestGUIProvider validationTestGUIProvider) {
		this.biomarker = biomarker;
		this.acceptRange = acceptRange;
		this.validationTestGUIProvider = validationTestGUIProvider;

		this.validationTestGUIProvider.declareVariablesInWizardModel(wizardModel());

		this.setWindowTitle(this.validationTestGUIProvider.getName() + " Validation");

		// Se invoca al replace porque la variable acceptRange no esta
		// inicializada al momento de crear el modelo.
		wizardModel().replace(PagesDescriptors.NUMBER_OF_CLUSTERS, getClusterWriatableValue());

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

	private List<OneBiomarkerCommand> commands2apply = Lists.newArrayList();

	public List<OneBiomarkerCommand> commands2apply() {
		return commands2apply;
	}

	@Override
	protected void doInUI(List<AbstractExperimentDescriptor> appliedExperiments) throws Exception {

		String numberOfClusters = getClusterRangeAsString();

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
		boolean removeInBiomarkerTheGenesThatAreNotInTheExperiment = wizardModel().value(PagesDescriptors.REMOVE_GENES_IN_GENE_SIGNATURE);

		String clusteringStrategy = wizardModel().value(PagesDescriptors.CLUSTERING_STRATEGY);
		IClusterer clusterer = ClustererFactory.getInstance().getClusterer(clusteringStrategy);

		// It adds additional parameters for the validation. It will delegate on
		// the validationTestGUIProvider
		Map<String, String> specificParametersForTheValidationTest = this.getSpecificParametersForTheValidationTest();

		for (AbstractExperimentDescriptor aed : appliedExperiments) {

			for (Integer clusters : calculateRange(numberOfClusters)) {

				final ValidationConfig4DoingCluster validationConfig = new ValidationConfig4DoingCluster(aed, clusters, attributeNameToValidation, secondAttributeNameToDoTheValidation, 1, removeInBiomarkerTheGenesThatAreNotInTheExperiment, clusterer);
				validationConfig.setSpecificParametersForTheValidationTest(specificParametersForTheValidationTest);

				commands2apply.addAll(this.createCommand(findBiomarker(), Lists.newArrayList(validationConfig)));

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

	private Map<String, String> getSpecificParametersForTheValidationTest() {

		Map<String, String> result = new HashMap<String, String>();
		this.validationTestGUIProvider.getSpecificParametersForTheValidationTest(result, wizardModel());

		return result;

	}

	public abstract List<OneBiomarkerCommand> createCommand(Biomarker findBiomarker, List<ValidationConfig4DoingCluster> list);

	protected String getClusterRangeAsString() {
		// TODO revisar cuando tira error de casting
		String cluster = null;
		if (acceptRange) {
			try {
				cluster = wizardModel().value(PagesDescriptors.NUMBER_OF_CLUSTERS);

			} catch (NumberFormatException e) {
				return wizardModel().value(PagesDescriptors.NUMBER_OF_CLUSTERS);
			}
		} else {
			cluster = String.valueOf(wizardModel().value(PagesDescriptors.NUMBER_OF_CLUSTERS));
		}
		return String.valueOf(cluster);
	}

	// TODO mejorar el nombre
	protected void afterExecution() {

	}

	// TODO mejorar el nombre
	protected void register(ValidationConfig4DoingCluster validationConfig) {

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
		// que es equivalente (a modo de homogeneizar el trato)
		if (!numberOfClusters.contains(".."))
			numberOfClusters = numberOfClusters + ".." + numberOfClusters;
		String[] r = numberOfClusters.split("\\.\\.");
		return ContiguousSet.create(Range.closed(new Integer(r[0]), new Integer(r[1])), DiscreteDomain.integers());

	}

	@Override
	protected WizardModel createWizardModel() {

		WizardModel wm = new WizardModel()//
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
				.add(PagesDescriptors.CLUSTERING_STRATEGY)//
		;

		WritableValue wv;
		wv = getClusterWriatableValue();
		wm.add(PagesDescriptors.NUMBER_OF_CLUSTERS, wv);

		//WritableValue wv4RepeatCLuster = new WritableValue(10, Integer.class);
		// wm.add(PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING,
		// wv4RepeatCLuster);
		// wm.set(PagesDescriptors.TIMES_TO_REPEAT_CLUSTERING, 10);

		return wm;
	}

	private WritableValue getClusterWriatableValue() {
		if (acceptRange)
			return new WritableValue("2..3", String.class);
		else
			return new WritableValue(2, Integer.class);
	}

	protected WritableValue clusterWritableValue() {
		return new WritableValue("2..3", String.class);
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		return Arrays.asList(PagesDescriptors.experimentsWPD(this.validationTestGUIProvider),//
				// new
				// CalculateClusterOrUseExistingPage("Choose if you want to use the existing cluster or calculate a new one"),//
				PagesDescriptors.configurationPage(this.validationTestGUIProvider));
	}

	@Override
	protected String getTaskName() {
		return "Add new validation to apply...";
	}

	@Override
	public boolean logInTheMessageView() {
		return false;
	}

}
