package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso;

import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion.MINIMUM_NUMBER_OF_GENES;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion.NUMBER_OF_PARTICLES;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion.NUMBER_OF_ROUNDS;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion.PROCESS_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.ValidationConfigPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.ValidationConfigWizard;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.optimizers.BiomarkerOptimizationResult;
import edu.unlp.medicine.domainLogic.framework.optimizers.IObjectWithFitness;
import edu.unlp.medicine.domainLogic.framework.optimizers.commands.PsoOptimizerCommand;
import edu.unlp.medicine.domainLogic.optimizations.configuration.PSOConfiguration;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.utils.monitor.Monitor;

/**
 * @author Diego Martínez
 */
public class PSOWizard extends AbstractWizard<BiomarkerOptimizationResult> implements Observer {

	public static final String TESTING_VALIDATION_CONFIG = "TESTING_VALIDATION_CONFIG";
	public static final String VALIDATION_VALIDATION_CONFIG = "VALIDATION_VALIDATION_CONFIG";
	public static final String TRAINING_VALIDATION_CONFIG = "TRAINING_VALIDATION_CONFIG";
	private Biomarker biomarker;
	private String processName;
	private int miniumNumberOfGenes, numberOfRounds, numberOfParticles, numberOfGenesToKeepDuringTraining;

	private List<ValidationConfig4DoingCluster> forTesting = new ArrayList<ValidationConfig4DoingCluster>();
	private List<ValidationConfig4DoingCluster> forTraining = new ArrayList<ValidationConfig4DoingCluster>();
	private List<ValidationConfig4DoingCluster> forValidation = new ArrayList<ValidationConfig4DoingCluster>();

	private PSOResultViewPart resultView;
	PsoOptimizerCommand optimizerCommand;

	public PSOWizard(Biomarker biomarker) {
		this.biomarker = biomarker;
		wizardModel().replace(MINIMUM_NUMBER_OF_GENES, new WritableValue(this.biomarker.getNumberOfGenes() / 3, Integer.class));

	}

	@Override
	protected ISchedulingRule getJobRule() {
		return ValidationConfigWizard.getMutexRule();
	}

	@Override
	public int getWizardWidth() {

		return 700;
	}

	@Override
	public int getWizardHeight() {

		return 680;
	}

	
	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		final ArrayList<WizardPageDescriptor> pages = Lists.newArrayList();
		pages.add(new PSOPage1Introduction());
		pages.add(new GeneralPSOConfigurarion());
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Training", "It is required. This is the dataset used for calculate metrics and compare Gene Sigantures", TRAINING_VALIDATION_CONFIG, Activator.imageDescriptorFromPlugin("experiment-training.png").createImage()).disableClusterRange().setImageDescriptor(Activator.imageDescriptorFromPlugin("pso.png")));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Testing", "If you set this dataset, it will be used for ordering the result. YOu should take the first one as the result of the algorithm.", TESTING_VALIDATION_CONFIG, Activator.imageDescriptorFromPlugin("experiment-testing.png").createImage()).optional().setImageDescriptor(Activator.imageDescriptorFromPlugin("pso.png")));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Validation", "This is the validation dataset. This woul be used for presenting results because it was independent of training and testing.", VALIDATION_VALIDATION_CONFIG, Activator.imageDescriptorFromPlugin("experiment-validation.png").createImage()).optional().setImageDescriptor(Activator.imageDescriptorFromPlugin("pso.png")));

		// pages.add(new ValidationConfigPageDescriptor(biomarker, "Training",
		// TRAINING_VALIDATION_CONFIG,null).disableClusterRange());
		// pages.add(new ValidationConfigPageDescriptor(biomarker, "Testing",
		// TESTING_VALIDATION_CONFIG,null).optional());
		// pages.add(new ValidationConfigPageDescriptor(biomarker, "Validation",
		// VALIDATION_VALIDATION_CONFIG,null).optional());

		// pages.add(new ValidationToDoPageDescriptor());
		return pages;
	}

	@Override
	protected String getTaskName() {
		return "PSO on " + biomarker;
	}

	@Override
	protected void doInUI(BiomarkerOptimizationResult result) throws Exception {
		PlatformUIUtils.openView(PSOResultViewPart.id());
		PSOResultViewPart v = (PSOResultViewPart) PlatformUIUtils.findView(PSOResultViewPart.id());

		v.setForTesting(null);
		v.setForValidation(null);
		v.setForTraining(forTraining.get(0));
		if (forTesting != null && forTesting.size() > 0)
			v.setForTesting(forTesting.get(0));
		if (forValidation != null && forValidation.size() > 0)
			v.setForValidation(forValidation.get(0));

		v.setResultToShow(result.getBettersTouchedDuringTheTrip());
	}

	@Override
	protected BiomarkerOptimizationResult backgroundProcess(Monitor monitor) throws Exception {
		optimizerCommand = new PsoOptimizerCommand(biomarker, new HashMap<String, String>());
		PSOConfiguration psoConfig = new PSOConfiguration();
		psoConfig.setName(processName);
		psoConfig.setNumberOfGenes(miniumNumberOfGenes);
		psoConfig.setNumberOfRounds(numberOfRounds);
		psoConfig.setNumberOfParticles(numberOfParticles);
		// optimizerCommand.setBettersSize(numberOfGenesToKeepDuringTraining);
		optimizerCommand.setConfiguration(psoConfig);

		optimizerCommand.setValidationConfig4CLustering4Training(forTraining.get(0));
		if (forValidation != null)
			optimizerCommand.setValidationConfig4Clustering4Validation(forValidation.get(0));
		if (forTesting != null)
			optimizerCommand.setValidationConfig4Clustering4Testing(forTesting.get(0));

		resultView = PlatformUIUtils.openView(PSOResultViewPart.id());
		resultView.setForTraining(forTraining.get(0));
		if (forTesting != null && forTesting.size() > 0)
			resultView.setForTesting(forTesting.get(0));
		if (forValidation != null && forValidation.size() > 0)
			resultView.setForValidation(forValidation.get(0));

		// va escuchando y completando con resultados a medida que los tiene
		optimizerCommand.addObserver(this);

		optimizerCommand.monitor(monitor).execute();
		return optimizerCommand.getPsoResult();
	}

	@Override
	protected WizardModel createWizardModel() {
		WizardModel wmodel = super.createWizardModel()//
				.add(PROCESS_NAME, String.class, "")//
				.add(MINIMUM_NUMBER_OF_GENES, Integer.class, 3)//
				.add(NUMBER_OF_ROUNDS, Integer.class, 100)//
				.add(NUMBER_OF_PARTICLES, Integer.class, 5)//
		//
		// .add(NUMBER_OF_GENE_SIGNATURE_TO_KEEP_DURING_TRAINING, Integer.class,
		// 10)//
		// No se agregan porque no son IObservables...
		// FIXME mejorar...
		// .add(TRAINING_VALIDATION_CONFIG, List.class, null). //
		// .add(VALIDATION_VALIDATION_CONFIG, List.class, null). //
		// .add(TESTING_VALIDATION_CONFIG, List.class, null) //
		;

		return wmodel;
	}

	@Override
	protected void configureParameters() {
		final WizardModel wm = wizardModel();
		processName = wm.value(PROCESS_NAME);
		miniumNumberOfGenes = wm.value(MINIMUM_NUMBER_OF_GENES);
		numberOfRounds = wm.value(NUMBER_OF_ROUNDS);
		numberOfParticles = wm.value(NUMBER_OF_PARTICLES);
		// numberOfGenesToKeepDuringTraining =
		// wm.value(NUMBER_OF_GENE_SIGNATURE_TO_KEEP_DURING_TRAINING);

		forTraining = wm.value(TRAINING_VALIDATION_CONFIG);
		forValidation = wm.value(VALIDATION_VALIDATION_CONFIG);
		forTesting = wm.value(TESTING_VALIDATION_CONFIG);

	}

	@Override
	public void update(Observable o, Object arg) {
		List<Biomarker> biomarkers = new ArrayList<Biomarker>();
		for (IObjectWithFitness biomarker : optimizerCommand.getActualBettersDuringTheTrip()) {
			biomarkers.add((Biomarker) biomarker);
		}

		resultView.updateResults(biomarkers);

	}
}
