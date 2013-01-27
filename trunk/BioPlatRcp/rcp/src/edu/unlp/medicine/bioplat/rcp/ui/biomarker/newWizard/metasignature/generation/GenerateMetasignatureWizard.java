package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.GMSPage4Algorithm.ALIX;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.GMSPage4Algorithm.ALIX_X_PARAMETER;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.GMSPage4Algorithm.CONT;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.GMSPage4Algorithm.IES;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.GMSPage4Algorithm.SEPARATOR;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.GMSPage4Algorithm.SMS;
import static edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.GMSPage4Algorithm.UNION;

import java.util.ArrayList;
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
import edu.unlp.medicine.domainLogic.framework.algorithm.IGeneSelectorAlgorithm;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.SingleMetasignatureGenerator;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;
import edu.unlp.medicine.entity.biomarker.MetaSignature;
import edu.unlp.medicine.utils.monitor.Monitor;

public class GenerateMetasignatureWizard extends AbstractWizard<MetaSignature> {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(GenerateMetasignatureWizard.class);
	List<GeneSignature> externalSelectedGS =  wizardModel().value(GMSPage2FIlterExternalDBs.SELECTED_SIGNATURES);
	List<GeneSignature> openedGeneSignatures =  wizardModel().value(GMSPage2FIlterExternalDBs.OPENED_BIOMARKERS);
	private IGeneSelectorAlgorithm algorithm = null;
	
	
	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> descriptors = Lists.newArrayList();
			
			descriptors.add(createIntroductionPage());
		
			descriptors.add(createExternalGSDatabasesPage());
			//There is no page for the selection of external geneSignatures because it is a resultPage of the Page2. This result page shows the GS which passed the filter and sets the SELECTED_SIGNATURES model whith this selection.    
		
			final List<Biomarker> openedBiomarkers = PlatformUIUtils.openedEditors(Biomarker.class);
			if (!openedBiomarkers.isEmpty()) descriptors.add(createLocalGS());
	
			descriptors.add(createAlgorithmPage());
				
		this.setWindowTitle("Generate a metasignature.");
		
		
		return descriptors;
	}

	private WizardPageDescriptor createLocalGS() {
		
		return new GMSPage3LocalGSs();
	}

	private GMSPage1Introduction createIntroductionPage() {
		return new GMSPage1Introduction();
	}

	private GMSPage2FIlterExternalDBs createExternalGSDatabasesPage() {
		return new GMSPage2FIlterExternalDBs(wizardModel());
	}

	private WizardPageDescriptor createAlgorithmPage() {
		return new GMSPage4Algorithm().addParameters(wizardModel());
	}

	private WizardPageDescriptor createValidationConfiguration() {
		return null;
	}

	@Override
	protected String getTaskName() {
		return "Generating Metasignature using " + algorithm.getName();
	}

	@Override
	protected MetaSignature backgroundProcess(Monitor m) throws Exception {
		SingleMetasignatureGenerator smg = new SingleMetasignatureGenerator();
		List<GeneSignature> inputGeneSignatures = new ArrayList<GeneSignature>(); 
		if (externalSelectedGS!=null) inputGeneSignatures.addAll(externalSelectedGS);
		if (openedGeneSignatures!=null) inputGeneSignatures.addAll(openedGeneSignatures);
		smg.setGeneSelectorAlgorithm(algorithm);
		return smg.calculateMetasignature(inputGeneSignatures);
	}

	@Override
	protected void doInUI(MetaSignature result) {
		PlatformUIUtils.openEditor(result, EditorsId.biomarkerEditorId());
	}

	@Override
	protected WizardModel createWizardModel() {
		return new WizardModel();

	}

	
	private String filter = "";

	@Override
	protected void configureParameters() {
		//geneProviders = gMSPage2FIlterExternalDBs.resolveProviders(wizardModel());
		algorithm = resolveAlgorithm();
		externalSelectedGS =  wizardModel().value(GMSPage2FIlterExternalDBs.SELECTED_SIGNATURES);
		
		 List<Biomarker> openedBiomarkers =  wizardModel().value(GMSPage2FIlterExternalDBs.OPENED_BIOMARKERS);
		 openedGeneSignatures = translateBiomarkerIntoGS(openedBiomarkers);
		
		
		//geneSignatures = wizardModel().value(GMSPage2FIlterExternalDBs.SELECTED_SIGNATURES);
		//filter = wizardModel().value(GMSPage2FIlterExternalDBs.ORGANISM);
	}

	private List<GeneSignature> translateBiomarkerIntoGS(
			List<Biomarker> openedBiomarkers) {
		
		
		
		List<GeneSignature> result = new ArrayList<GeneSignature>();
		if (openedBiomarkers!=null){
			for (Biomarker biomarker : openedBiomarkers) {
				result.add(new GeneSignature(biomarker));
		}}
		return result;
	}

	private IGeneSelectorAlgorithm resolveAlgorithm() {
		String from = wizardModel().value(GMSPage4Algorithm.ALGORITHM);
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


	
	
}
