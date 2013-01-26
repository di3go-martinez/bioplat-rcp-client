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
	private Filters filters;

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> descriptors = Lists.newArrayList();
		
		descriptors.add(createProvidersPage());
		descriptors.add(filters = createFilterPage());
		descriptors.add(createAlgorithmPage());
		// descriptors.add(createValidationConfiguration());
		
		this.setWindowTitle("Generate a metasignature. Select the input Gene Signatures and the algorithm which will suggest genes of your metasignature.");
		
		return descriptors;
	}

	private Providers createProvidersPage() {
		return wpdproviders = new Providers();//.addParameters(wizardModel());
	}

	private Filters createFilterPage() {
		return new Filters().addParameters(wizardModel());
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

		smg.setGeneSelectorAlgorithm(algorithm);

		// TODO Validation phase

		if (geneSignatures != null && !geneSignatures.isEmpty())
			return smg.calculateMetasignature(geneSignatures);
		else
			// TODO sacar que esta es la forma vieja y no debería pasar más por
			// acá ahora que tiene resultado intermedio
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
	private String filter = "";

	@Override
	protected void configureParameters() {
		geneProviders = filters.resolveProviders(wizardModel());
		algorithm = resolveAlgorithm();
		geneSignatures = wizardModel().value(Filters.SELECTED_SIGNATURES);
		filter = wizardModel().value(Filters.ORGANISM);
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
