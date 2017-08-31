package org.bioplat.classifiers.newWizards.evaluation;

import static edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message.error;
import static edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message.info;

import java.util.List;
import java.util.Map;

import org.eclipse.ui.INewWizard;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.core.preferences.AuthorPreferencePage;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.domainLogic.framework.classifiers.Classifier;
import edu.unlp.medicine.domainLogic.framework.classifiers.ClassifierExecutionResult;
import edu.unlp.medicine.domainLogic.framework.classifiers.EvaluateClassifier;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.utils.monitor.Monitor;

public class EvaluateClassifierWizard extends AbstractWizard<ClassifierExecutionResult> implements INewWizard {

	public static final String CLASSIFIER = "CLASSIFIER";
	public static final String SAMPLE = "SAMPLE";

	public EvaluateClassifierWizard() {
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		return Lists.<WizardPageDescriptor>newArrayList(//
				new EvaluateClassifierPageDescriptor(),
				new SelectClassifierPageDescriptor("Select Classifier by Author", author()));
	}

	private String author() {
		return AuthorPreferencePage.author();
	}

	@Override
	protected String getTaskName() {
		return "Classifier Evaluation";
	}

	@Override
	protected ClassifierExecutionResult backgroundProcess(Monitor monitor) throws Exception {
		Classifier classifier = value(CLASSIFIER);
		Map<Gene, Double> sample = value(SAMPLE);
		return new EvaluateClassifier().evaluate(classifier, sample);
	}

	

	@Override
	protected void doInUI(ClassifierExecutionResult result) throws Exception {
		MessageManager.INSTANCE.add(info(result.message()));
	}

	@Override
	public int getWizardWidth() {
		return 400;
	}
}
