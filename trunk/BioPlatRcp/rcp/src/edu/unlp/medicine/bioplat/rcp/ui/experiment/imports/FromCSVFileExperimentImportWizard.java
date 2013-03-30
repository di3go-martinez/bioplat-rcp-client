package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromCSVFile.FromCSVFilePage2Main;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromCSVFile.FromCSVFilePage3SelectGenesOrGSForFiltering;
import edu.unlp.medicine.bioplat.rcp.ui.utils.wizards.GenericPage1ForIntroduction;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.FromFileExperimentDescriptor;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.FromFileExperimentFactory;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.utils.monitor.Monitor;

public class FromCSVFileExperimentImportWizard extends AbstractWizard<Experiment> {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(FromCSVFileExperimentImportWizard.class);

	//Variables used in the wizard
	List<GeneSignature> openedSelectedGeneSignatures =  wizardModel().value(FromCSVFilePage3SelectGenesOrGSForFiltering.OPENED_SELECTED_BIOMARKERS);
	String filePath;
	String collapseStrategy;
	int clinicalDataFirstLine;
	
	
	public FromCSVFileExperimentImportWizard() {
		super();
		
		
	}
	
	
	@Override
	public int getWizardHeight() {
		
		return 630;
	}
	
	
	@Override
	public int getWizardWidth() {
		
		return 630;
	}
	
	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> descriptors = Lists.newArrayList();
			
			descriptors.add(createIntroductionPage());
		
			descriptors.add(createMainPage());
		
			//final List<Biomarker> openedBiomarkers = PlatformUIUtils.openedEditors(Biomarker.class);
			descriptors.add(createPage3ForSelecctingGenesOrGS());
	
				
		this.setWindowTitle("Generate a metasignature.");
		
		
		return descriptors;
	}

	private WizardPageDescriptor createMainPage() {
		return new FromCSVFilePage2Main(wizardModel());
	}

	private WizardPageDescriptor createPage3ForSelecctingGenesOrGS() {
		
		return new FromCSVFilePage3SelectGenesOrGSForFiltering();
	}

	private GenericPage1ForIntroduction createIntroductionPage() {
		
		String inBlankBigTitle =  "Import experiment from .CSV file"; 

		String inBlankSmallTitle =  "Import an experiment, from a text file containing expression data and clinical data";

		String introductionText =  "An experiment includes expression data and clinical data. An experiment in Bioplat can be used for validating biomarkers, for optimizing biomarkers or just for filtering and visualizing the experiment data in a convenient way. You can also export the experiment into a file.\n\n If your experiment is too large, you should use the last page of this wizard for filtering the genes to import. You can do it pasting the gene list or selecting an opened Gene Signature.\n\nYou can select the collapsing strategy (media, median or variance). It will be used for picking up the gene row if there is more than one row for this gene.";
		
		return new GenericPage1ForIntroduction(inBlankBigTitle, inBlankSmallTitle, introductionText);
	}


	private WizardPageDescriptor createValidationConfiguration() {
		return null;
	}

	@Override
	protected String getTaskName() {
		return "Import experiment from CSV file";
	}
	
	
	@Override
	protected void configureParameters() {
		
		 List<Biomarker> openedBiomarkers =  wizardModel().value(FromCSVFilePage3SelectGenesOrGSForFiltering.OPENED_SELECTED_BIOMARKERS);
		 openedSelectedGeneSignatures = translateBiomarkerIntoGS(openedBiomarkers);
		
		 filePath = wizardModel().value(FromCSVFilePage2Main.FILE_PATH);
		 collapseStrategy = wizardModel().value(FromCSVFilePage2Main.COLLAPSE_STRATEGY);
		 
		 //final String clinicalDataFirstLineString = wizardModel().value(FromCSVFilePage2Main.CLINICAL_FIRST_LINE);
		 clinicalDataFirstLine = wizardModel().value(FromCSVFilePage2Main.CLINICAL_FIRST_LINE);

	}


	@Override
	protected Experiment backgroundProcess(Monitor m) throws Exception {
		
		FromFileExperimentDescriptor fromFileExperimentDescriptor = new FromFileExperimentDescriptor(filePath, 1, clinicalDataFirstLine-2, clinicalDataFirstLine-1, clinicalDataFirstLine, "\t", collapseStrategy);
		
		java.util.List<Gene> genesToKeep = openedSelectedGeneSignatures.get(0).getGenes();
		
		//add55gmGenes(genesToKeep);
		
		fromFileExperimentDescriptor.setGenesToKeep(genesToKeep);
		
		return new FromFileExperimentFactory(fromFileExperimentDescriptor).monitor(m).createExperiment();

		
	}
		
		
	@Override
	protected void doInUI(Experiment result) {
		try {
			final Experiment e = result; // join
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					PlatformUIUtils.openEditor(e, ExperimentEditor.id());
					MessageManager.INSTANCE.openView().add(Message.info("Experiment from file \"" + filePath + "\" was imported sucessfully. There were " + e.getNumberOfCollapsedGenes() + " collapsed genes. They were collapsed using " + e.getCollapsedStrategyName() + "."));
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected WizardModel createWizardModel() {
		return new WizardModel();

	}

	
	private String filter = "";


	private List<GeneSignature> translateBiomarkerIntoGS(
			List<Biomarker> openedBiomarkers) {
		
		
		
		List<GeneSignature> result = new ArrayList<GeneSignature>();
		if (openedBiomarkers!=null){
			for (Biomarker biomarker : openedBiomarkers) {
				result.add(new GeneSignature(biomarker));
		}}
		return result;
	}



	
	
}
