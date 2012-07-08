package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.contributions.TreeACResultViewPart;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.PSOResultViewPart;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.ValidationConfigPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.VariationsOfMetasignatureInDepthCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.ValidationConfig;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.utils.monitor.Monitor;

public class TreeWizard extends AbstractWizard<VariationsOfMetasignatureInDepthCommand> {
	private static final String NUMBER_OF_TOP_RESULTS_TO_KEEP_IN_EACH_ROUND = "NumberOfTopResultsToKeepInEachRound";
	private static final String NUMBER_OF_GENES_OF_THE_SMALLEST_BIOMARKER = "NumberOfGenesOfTheSmallestBiomarker";
	public static final String TESTING_VALIDATION_CONFIG = "TESTING_VALIDATION_CONFIG";
	public static final String VALIDATION_VALIDATION_CONFIG = "VALIDATION_VALIDATION_CONFIG";
	public static final String TRAINING_VALIDATION_CONFIG = "TRAINING_VALIDATION_CONFIG";
	private List<ValidationConfig> forTesting, forTraining, forValidation;
	private Integer numberOfGenesTheSmallestBiomarker;
	private Integer numberOfTopResultsToKeepInEachRound;
	private Biomarker biomarker;

	public TreeWizard(Biomarker model) {
		this.biomarker = model;
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		final ArrayList<WizardPageDescriptor> pages = Lists.newArrayList();
		pages.add(new GeneralTreeConfigurarionPageDescriptor().addParameters(wizardModel()));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Training", TRAINING_VALIDATION_CONFIG));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Validation", VALIDATION_VALIDATION_CONFIG));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Testing", TESTING_VALIDATION_CONFIG));
		// pages.add(new ValidationToDoPageDescriptor());
		return pages;
	}

	@Override
	protected String getTaskName() {
		return "Calculating variations on the Gene signature " + biomarker;
	}

	@Override
	protected VariationsOfMetasignatureInDepthCommand backgroundProcess(Monitor monitor) throws Exception {
		Map<String, String> properties = new HashMap<String, String>();

		VariationsOfMetasignatureInDepthCommand optimizerCommand = new VariationsOfMetasignatureInDepthCommand(biomarker, properties);

		optimizerCommand.setNumberOfGenesOfTheSmallestBiomarker(numberOfGenesTheSmallestBiomarker);
		optimizerCommand.setNumberOfTopResultsToKeepInEachRound(numberOfTopResultsToKeepInEachRound);

		optimizerCommand.setValidationConfigs4Training(forTraining);
		optimizerCommand.setValidationConfigs4Validation(forValidation);
		optimizerCommand.setValidationConfigs4Testing(forTesting);

		optimizerCommand.monitor(monitor).execute();

		return optimizerCommand;

	}

	@Override
	protected void doInUI(VariationsOfMetasignatureInDepthCommand result) throws Exception {
		List<Biomarker> bestVariations = result.getBettersBiomarkersDuringTheTrip();
		PlatformUIUtils.openView(TreeACResultViewPart.id());
		TreeACResultViewPart v = PlatformUIUtils.findView(PSOResultViewPart.id());
		v.setResultToShow(bestVariations);
	}

	@Override
	protected void configureParameters() {

		numberOfGenesTheSmallestBiomarker = wizardModel().value(GeneralTreeConfigurarionPageDescriptor.NUMBER_OF_GENES);
		numberOfTopResultsToKeepInEachRound = wizardModel().value(GeneralTreeConfigurarionPageDescriptor.NUM_GENE_SIGNATURES_TO_KEEP);

		forTraining = wizardModel().value(TRAINING_VALIDATION_CONFIG);
		forValidation = wizardModel().value(VALIDATION_VALIDATION_CONFIG);
		forTesting = wizardModel().value(TESTING_VALIDATION_CONFIG);
	}

}
