package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.AlgorithmPageDescriptor.ALIX;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.AlgorithmPageDescriptor.ALIX_X_PARAMETER;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.AlgorithmPageDescriptor.CONT;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.AlgorithmPageDescriptor.IES;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.AlgorithmPageDescriptor.SEPARATOR;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.AlgorithmPageDescriptor.SMS;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.AlgorithmPageDescriptor.UNION;

import java.util.List;

import org.slf4j.Logger;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.ext.algorithms.ALIX;
import edu.unlp.medicine.domainLogic.ext.algorithms.CONT;
import edu.unlp.medicine.domainLogic.ext.algorithms.IES;
import edu.unlp.medicine.domainLogic.ext.algorithms.SMS;
import edu.unlp.medicine.domainLogic.ext.algorithms.Union;
import edu.unlp.medicine.domainLogic.framework.GeneSignatureProvider.IGeneSignatureProvider;
import edu.unlp.medicine.domainLogic.framework.algorithm.IGeneSelectorAlgorithm;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.SingleMetasignatureGenerator;
import edu.unlp.medicine.entity.biomarker.GeneSignature;
import edu.unlp.medicine.entity.biomarker.MetaSignature;
import edu.unlp.medicine.utils.monitor.Monitor;

public class GenerateMetasignatureWizard extends AbstractWizard<MetaSignature> {

	private Providers wpdproviders;

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> descriptors = Lists.newArrayList();

		Providers p;
		descriptors.add(p = createProvidersPage());
		descriptors.add(createFilterPage(p));
		descriptors.add(createAlgorithmPage());
		// descriptors.add(createValidationConfiguration());

		return descriptors;
	}

	private Providers createProvidersPage() {
		return wpdproviders = new Providers().addParameters(wizardModel());
	}

	private WizardPageDescriptor createFilterPage(Providers providers) {
		return new Filters(providers).addParameters(wizardModel());
	}

	private WizardPageDescriptor createAlgorithmPage() {
		return new AlgorithmPageDescriptor().addParameters(wizardModel());
	}

	private WizardPageDescriptor createValidationConfiguration() {
		return null;
	}

	@Override
	protected String getTaskName() {
		return "Metasignature Calculator using " + algorithm.getName();
	}

	@Override
	protected MetaSignature backgroundProcess(Monitor m) throws Exception {
		SingleMetasignatureGenerator smg = new SingleMetasignatureGenerator();

		for (IGeneSignatureProvider gsp : geneProviders)
			smg.addGeneSigntaureProvider(gsp);

		// TODO filter
		smg.setGeneSelectorAlgorithm(algorithm);
		// TODO Validation
		if (geneSignatures != null && !geneSignatures.isEmpty())
			return smg.calculateMetasignature(geneSignatures);
		else
			return smg.generateMetaSignature();
	}

	@Override
	protected void doInUI(MetaSignature result) {
		PlatformUIUtils.openEditor(result, EditorsId.biomarkerEditorId());
	}

	@Override
	protected WizardModel createWizardModel() {
		return new WizardModel();

	}

	private IGeneSelectorAlgorithm algorithm;

	@Override
	protected void configureParameters() {
		geneProviders = wpdproviders.resolveProviders(wizardModel());
		algorithm = resolveAlgorithm();
		geneSignatures = wizardModel().value(Filters.SELECTED_SIGNATURES);
	}

	private IGeneSelectorAlgorithm resolveAlgorithm() {
		String from = wizardModel().value(AlgorithmPageDescriptor.ALGORITHM);
		IGeneSelectorAlgorithm result = null;
		if (from == null)
			result = null;

		if (from.equals(ALIX))
			result = new ALIX((Integer) (wizardModel().value(ALIX + SEPARATOR + ALIX_X_PARAMETER)));
		else if (from.equals(UNION))
			result = new Union();
		else if (from.equals(IES))
			result = new IES();
		else if (from.equals(SMS))
			result = new SMS();
		else if (from.equals(CONT))
			result = new CONT();

		return result;
	}

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(GenerateMetasignatureWizard.class);
	private List<IGeneSignatureProvider> geneProviders;
	private List<GeneSignature> geneSignatures;
}