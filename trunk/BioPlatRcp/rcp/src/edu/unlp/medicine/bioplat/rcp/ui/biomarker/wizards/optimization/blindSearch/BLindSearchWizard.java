package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.blindSearch;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.ValidationConfigPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.BlindSearchOptimizerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.LogRankTestValidationConfig;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.utils.monitor.Monitor;

/**
 * @author Diego Martínez
 */
public class BLindSearchWizard extends AbstractWizard<List<Biomarker>> {

	public static final String TESTING_VALIDATION_CONFIG = "TESTING_VALIDATION_CONFIG";
	public static final String VALIDATION_VALIDATION_CONFIG = "VALIDATION_VALIDATION_CONFIG";
	public static final String TRAINING_VALIDATION_CONFIG = "TRAINING_VALIDATION_CONFIG";

	
	
	
	

	private List<LogRankTestValidationConfig> forTesting=new ArrayList<LogRankTestValidationConfig>();
	private List<LogRankTestValidationConfig> forTraining=new ArrayList<LogRankTestValidationConfig>();
	private List<LogRankTestValidationConfig> forValidation=new ArrayList<LogRankTestValidationConfig>();
	private Biomarker biomarker;
	int numberOfGenesToRemoveFrom;
	int numberOfGenesToRemoveTo;
	int numberOfResultsToShow;

	

	public BLindSearchWizard(Biomarker biomarker) {
		this.biomarker = biomarker;
		this.setWindowTitle("Blind search optimizer");
		wizardModel().add(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_TO_REMOVE_TO, Integer.class, biomarker.getNumberOfGenes());
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		final ArrayList<WizardPageDescriptor> pages = Lists.newArrayList();
		pages.add(new GeneralBlindSearchConfiguration());
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Training", TRAINING_VALIDATION_CONFIG).disableClusterRange());
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Testing", TESTING_VALIDATION_CONFIG).optional());
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Validation", VALIDATION_VALIDATION_CONFIG).optional());
		// pages.add(new ValidationToDoPageDescriptor());
		return pages;
	}

	@Override
	protected String getTaskName() {
		return "Blind Search on " + biomarker;
	}


	@Override
	protected List<Biomarker> backgroundProcess(Monitor monitor) throws Exception {
		BlindSearchOptimizerCommand blindSearchCommand = new BlindSearchOptimizerCommand(biomarker, new HashMap<String, String>());
		blindSearchCommand.setNumberOfGenesToRemoveFrom(numberOfGenesToRemoveFrom);
		blindSearchCommand.setNumberOfGenesToRemoveTo(numberOfGenesToRemoveTo);
		

		
		blindSearchCommand.setLogRankTestValidationConfig4Training(forTraining.get(0));
		if (forValidation != null) blindSearchCommand.setLogRankTestValidationConfig4Validation(forValidation.get(0));
		if (forTesting != null) blindSearchCommand.setLogRankTestValidationConfig4Testing(forTesting.get(0));
		
		//blindSearchCommand.execute();
		
		blindSearchCommand.monitor(monitor).execute();

		return blindSearchCommand.getBetters(numberOfResultsToShow);
		
	}

	@Override
	protected WizardModel createWizardModel() {
		WizardModel wmodel = super.createWizardModel()//
				.add(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_TO_REMOVE_FROM, Integer.class, 1)//
				.add(GeneralBlindSearchConfiguration.NUMBER_OF_RESULTS_TO_SHOW, Integer.class, 10)//
				//.add(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_TO_REMOVE_TO, Integer.class, 20);
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
//		processName = wm.value(PROCESS_NAME);
//		miniumNumberOfGenes = wm.value(MINIMUM_NUMBER_OF_GENES);
//		numberOfRounds = wm.value(NUMBER_OF_ROUNDS);
//		numberOfParticles = wm.value(NUMBER_OF_PARTICLES);
//		numberOfGenesToKeepDuringTraining = wm.value(NUMBER_OF_GENES_TO_KEEP_DURING_TRAINING);

		
		numberOfGenesToRemoveFrom = wm.value(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_TO_REMOVE_FROM);
		numberOfGenesToRemoveTo = wm.value(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_TO_REMOVE_TO);
		numberOfResultsToShow = wm.value(GeneralBlindSearchConfiguration.NUMBER_OF_RESULTS_TO_SHOW);
		
		forTraining = wm.value(TRAINING_VALIDATION_CONFIG);
		forValidation = wm.value(VALIDATION_VALIDATION_CONFIG);
		forTesting = wm.value(TESTING_VALIDATION_CONFIG);

	}

	@Override
	protected void doInUI(List<Biomarker> result) throws Exception {
		PlatformUIUtils.openView(BlindSearchResultViewPart.id());
		BlindSearchResultViewPart v = (BlindSearchResultViewPart) PlatformUIUtils.findView(BlindSearchResultViewPart.id());
		v.setResultToShow(result);
		
	}
}
