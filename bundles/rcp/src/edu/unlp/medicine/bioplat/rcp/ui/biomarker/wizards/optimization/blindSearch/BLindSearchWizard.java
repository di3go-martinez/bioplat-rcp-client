package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.blindSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.dialogs.MessageDialog;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.ValidationConfigPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.ValidationConfigWizard;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.optimizers.blindSearch.BlindSearchOptimizerCommand;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.optimizers.blindSearch.BlindSearchResult;
import edu.unlp.medicine.domainLogic.framework.exceptions.BioplatWarning;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.ClusteringException;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.utils.monitor.Monitor;


public class BLindSearchWizard extends AbstractWizard<BlindSearchResult> implements Observer {

	public static final String TESTING_VALIDATION_CONFIG = "TESTING_VALIDATION_CONFIG";
	public static final String VALIDATION_VALIDATION_CONFIG = "VALIDATION_VALIDATION_CONFIG";
	public static final String TRAINING_VALIDATION_CONFIG = "TRAINING_VALIDATION_CONFIG";

	private List<ValidationConfig4DoingCluster> forTesting = new ArrayList<ValidationConfig4DoingCluster>();
	private List<ValidationConfig4DoingCluster> forTraining = new ArrayList<ValidationConfig4DoingCluster>();
	private List<ValidationConfig4DoingCluster> forValidation = new ArrayList<ValidationConfig4DoingCluster>();
	private Biomarker biomarker;
	int numberOfGenesForResultingGSFrom;
	int numberOfGenesForResultingGSTo;
	int numberOfResultsToShow;

	public BLindSearchWizard(Biomarker biomarker) {
		this.biomarker = biomarker;
		this.setWindowTitle("Blind search optimizer");
		wizardModel().add(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_FROM, Integer.class, biomarker.getNumberOfGenes() - 5 > 0 ? biomarker.getNumberOfGenes() - 5 : 1);
		wizardModel().add(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_TO, Integer.class, biomarker.getNumberOfGenes());

	}

	@Override
	public int getWizardWidth() {

		return 700;
	}

	@Override
	public int getWizardHeight() {

		return 650;
	}

	@Override
	protected ISchedulingRule getJobRule() {
		return ValidationConfigWizard.getMutexRule();
	}


	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		final ArrayList<WizardPageDescriptor> pages = Lists.newArrayList();
		pages.add(new BlindSearchPage1Introduction());
		pages.add(new GeneralBlindSearchConfiguration(biomarker));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Training", "This is required. This is the experiment used for calculating the prognostic value metric and comparing Gene Sigantures.", TRAINING_VALIDATION_CONFIG, Activator.imageDescriptorFromPlugin("experiment-training.png").createImage()).disableClusterRange().setImageDescriptor(Activator.imageDescriptorFromPlugin("blindSearch-page.png")));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Testing", "If you set this dataset, it will be used for ordering the result. It is a good pratice to take the result with the best valdation result for picking up your GeneSignature.", TESTING_VALIDATION_CONFIG, Activator.imageDescriptorFromPlugin("experiment-testing.png").createImage()).optional().setImageDescriptor(Activator.imageDescriptorFromPlugin("blindSearch-page.png")));
		pages.add(new ValidationConfigPageDescriptor(biomarker, "Validation", "This is the validation dataset. This would be used for presenting results because it was independent from training and testing.", VALIDATION_VALIDATION_CONFIG, Activator.imageDescriptorFromPlugin("experiment-validation.png").createImage()).optional().setImageDescriptor(Activator.imageDescriptorFromPlugin("blindSearch-page.png")));

		// pages.add(new ValidationConfigPageDescriptor(biomarker, "Training",
		// TRAINING_VALIDATION_CONFIG,
		// Activator.imageDescriptorFromPlugin("experiment-Training.png").createImage()).disableClusterRange());
		// pages.add(new ValidationConfigPageDescriptor(biomarker, "Testing",
		// TESTING_VALIDATION_CONFIG,
		// Activator.imageDescriptorFromPlugin("experiment-Testing.png").createImage()).optional());
		// pages.add(new ValidationConfigPageDescriptor(biomarker, "Validation",
		// VALIDATION_VALIDATION_CONFIG,Activator.imageDescriptorFromPlugin("experiment-validation.png").createImage()).optional());

		return pages;
	}

	@Override
	protected String getTaskName() {
		return "Blind Search on " + biomarker;
	}

	private BlindSearchOptimizerCommand blindSearchCommand = null;
	private BlindSearchResultViewPart resultView;

	@Override
	protected BlindSearchResult backgroundProcess(Monitor monitor) throws Exception {

		blindSearchCommand = new BlindSearchOptimizerCommand(biomarker, new HashMap<String, String>());
		blindSearchCommand.setNumberOfGenesToRemoveFrom(biomarker.getNumberOfGenes() - numberOfGenesForResultingGSTo + 1);
		blindSearchCommand.setNumberOfGenesToRemoveTo(biomarker.getNumberOfGenes() - numberOfGenesForResultingGSFrom);
		blindSearchCommand.setNumberOfResults(numberOfResultsToShow);

		resultView = PlatformUIUtils.openView(BlindSearchResultViewPart.id());
		resultView.setForTraining(forTraining.get(0));
		if (forTesting != null && forTesting.size() > 0)
			resultView.setForTesting(forTesting.get(0));
		if (forValidation != null && forValidation.size() > 0)
			resultView.setForValidation(forValidation.get(0));

		// va escuchando y completando con resultados a medida que los tiene
		blindSearchCommand.addObserver(this);

		blindSearchCommand.setValidationConfig4Clustering4Training(forTraining.get(0));
		if (forValidation != null)
			blindSearchCommand.setValidationConfig4Clustering4Validation(forValidation.get(0));
		if (forTesting != null)
			blindSearchCommand.setValidationConfig4Clustering4Testing(forTesting.get(0));

		try {
			blindSearchCommand.monitor(monitor).execute();
		} catch (ClusteringException e) {
			Message errorMsg;
			String errorText = e.getSpecificError() + ". Details: " + e.getGenericError();
			errorMsg = (e.isWarning()) ? Message.warn(errorText) : Message.error(errorText);
			this.setErrorMessage(errorMsg);
			throw e;
		}
		return blindSearchCommand.getBlindSearchResult();

	}

	@Override
	protected WizardModel createWizardModel() {
		WizardModel wmodel = super.createWizardModel()//
				.add(GeneralBlindSearchConfiguration.NUMBER_OF_RESULTS_TO_SHOW, Integer.class, 10)//
		// .add(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_TO_REMOVE_TO,
		// Integer.class, 20);
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
		// processName = wm.value(PROCESS_NAME);
		// miniumNumberOfGenes = wm.value(MINIMUM_NUMBER_OF_GENES);
		// numberOfRounds = wm.value(NUMBER_OF_ROUNDS);
		// numberOfParticles = wm.value(NUMBER_OF_PARTICLES);
		// numberOfGenesToKeepDuringTraining =
		// wm.value(NUMBER_OF_GENES_TO_KEEP_DURING_TRAINING);

		numberOfGenesForResultingGSFrom = wm.value(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_FROM);
		numberOfGenesForResultingGSTo = wm.value(GeneralBlindSearchConfiguration.NUMBER_OF_GENES_TO);
		numberOfResultsToShow = wm.value(GeneralBlindSearchConfiguration.NUMBER_OF_RESULTS_TO_SHOW);

		forTraining = wm.value(TRAINING_VALIDATION_CONFIG);
		forValidation = wm.value(VALIDATION_VALIDATION_CONFIG);
		forTesting = wm.value(TESTING_VALIDATION_CONFIG);

	}

	@Override
	protected void doInUI(BlindSearchResult result) throws Exception {

		BlindSearchResultViewPart v = (BlindSearchResultViewPart) PlatformUIUtils.findView(BlindSearchResultViewPart.id());

		v.setResultToShow(result.getBetters());

		if (result.getWarnings().size() > 0) {
			String warningMsg = "Blind Search on " + biomarker.getName() + " has finished but there were some warnings. You can see results on 'Blind Search Result' view but please read carefully the warning details in the message view, before analyzing the result";
			MessageDialog.openWarning(PlatformUIUtils.findShell(), "Blind Search has finished with warnings",
					warningMsg);
			MessageManager.INSTANCE.add(Message.warn(warningMsg));
		} else {

			String okMsg = "Blind search was succesfully executed on " + biomarker.getName() + ". See results in the 'Blind Search Result' View.";
			MessageDialog.openInformation(PlatformUIUtils.findShell(), "Blind Search succesfully executed", okMsg);
			MessageManager.INSTANCE.add(Message.info(okMsg));
		}

	}

	@Override
	public void addMessageToMessageView(BlindSearchResult result) {

		for (BioplatWarning warn : result.getWarnings()) {
			MessageManager.INSTANCE.add(Message.warn(warn.getMessage() + " Details. " + warn.getDetails()));
		}

	}

	@Override
	public void doInUIError(Throwable e) throws Exception {

		// MessageDialog.openError(PlatformUIUtils.findShell(), "Warning",
		// e.getMessage());

	}

	@Override
	public void update(Observable o, Object arg) {

		resultView.updateResults(blindSearchCommand.getBlindSearchResult().getBetters());
	}

}
