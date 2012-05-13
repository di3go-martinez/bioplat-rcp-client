package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso;

import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion.MINIMUM_NUMBER_OF_GENES;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion.NUMBER_OF_GENES_TO_KEEP_DURING_TRAINING;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion.NUMBER_OF_PARTICLES;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion.NUMBER_OF_ROUNDS;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion.PROCESS_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.page.descriptors.GeneralPSOConfigurarion;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.ValidationConfig;
import edu.unlp.medicine.domainLogic.framework.optimizers.BiomarkerOptimizationResult;
import edu.unlp.medicine.domainLogic.framework.optimizers.commands.PsoOptimizerCommand;
import edu.unlp.medicine.domainLogic.optimizations.configuration.PSOConfiguration;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.utils.monitor.Monitor;

/**
 * @author Diego Martínez
 */
public class PSOWizard extends AbstractWizard<BiomarkerOptimizationResult> {

	public static final String TESTING_VALIDATION_CONFIG = "TESTING_VALIDATION_CONFIG";
	public static final String VALIDATION_VALIDATION_CONFIG = "VALIDATION_VALIDATION_CONFIG";
	public static final String TRAINING_VALIDATION_CONFIG = "TRAINING_VALIDATION_CONFIG";
	private Biomarker biomarker;
	private String processName;
	private int miniumNumberOfGenes, numberOfRounds, numberOfParticles, numberOfGenesToKeepDuringTraining;

	private List<ValidationConfig> forTesting, forTraining, forValidation;

	public PSOWizard(Biomarker biomarker) {
		this.biomarker = biomarker;
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		final ArrayList<WizardPageDescriptor> pages = Lists.newArrayList();
		pages.add(new GeneralPSOConfigurarion());
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Training", TRAINING_VALIDATION_CONFIG));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Validation", VALIDATION_VALIDATION_CONFIG));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Testing", TESTING_VALIDATION_CONFIG));
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
		v.setResultToShow(result);
	}

	@Override
	protected BiomarkerOptimizationResult backgroundProcess(Monitor monitor) throws Exception {
		PsoOptimizerCommand optimizerCommand = new PsoOptimizerCommand(biomarker, new HashMap<String, String>());
		PSOConfiguration psoConfig = new PSOConfiguration();
		psoConfig.setName(processName);
		psoConfig.setNumberOfGenes(miniumNumberOfGenes);
		psoConfig.setNumberOfRounds(numberOfRounds);
		psoConfig.setNumberOfParticles(numberOfParticles);
		optimizerCommand.setBettersSize(numberOfGenesToKeepDuringTraining);
		optimizerCommand.setConfiguration(psoConfig);

		optimizerCommand.setValidationConfigsForTraining(forTraining);
		optimizerCommand.setValidationConfigsForValidation(forValidation);
		optimizerCommand.setValidationConfigsForTesting(forTesting);

		optimizerCommand.execute();
		return optimizerCommand.getPsoResult();
	}

	@Override
	protected WizardModel createWizardModel() {
		WizardModel wmodel = super.createWizardModel()//
				.add(PROCESS_NAME, String.class, "")//
				.add(MINIMUM_NUMBER_OF_GENES, Integer.class, 30)//
				.add(NUMBER_OF_ROUNDS, Integer.class, 1000)//
				.add(NUMBER_OF_PARTICLES, Integer.class, 15)//
				.add(NUMBER_OF_GENES_TO_KEEP_DURING_TRAINING, Integer.class, 10)//
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
		processName = wizardModel().value(PROCESS_NAME);
		miniumNumberOfGenes = wizardModel().value(MINIMUM_NUMBER_OF_GENES);
		numberOfRounds = wizardModel().value(NUMBER_OF_ROUNDS);
		numberOfParticles = wizardModel().value(NUMBER_OF_PARTICLES);
		numberOfGenesToKeepDuringTraining = wizardModel().value(NUMBER_OF_GENES_TO_KEEP_DURING_TRAINING);

		forTraining = wizardModel().value(TRAINING_VALIDATION_CONFIG);
		forValidation = wizardModel().value(VALIDATION_VALIDATION_CONFIG);
		forTesting = wizardModel().value(TESTING_VALIDATION_CONFIG);

	}
}