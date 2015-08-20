package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA.ImportExperimentFromTCGATestPage2;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA.ImportExperimentFromTCGATestPage3;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA.ImportExperimentFromTCGATestPage4;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA.ImportExperimentFromTCGATestPage5;
import edu.unlp.medicine.bioplat.rcp.ui.utils.wizards.GenericPage1ForIntroduction;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.domainLogic.framework.exceptions.GeneNotFoundByIdException;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.FromTCGAExperimentDescriptor;
import edu.unlp.medicine.entity.experiment.AbstractExperimentFactory;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.FromTCGAExperimentFactory;
import edu.unlp.medicine.entity.experiment.tcga.api.TCGAApi;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.r4j.values.R4JStringDataMatrix;
import edu.unlp.medicine.utils.monitor.Monitor;

public class ImportExperimentFromTCGATest  extends AbstractWizard<Experiment> {
	
	private static Logger logger = LoggerFactory.getLogger(ImportExperimentFromTCGATest.class);
	public static String STUDY = "STUDY";
	public static String PROFILE = "PROFILE";
	public static String CASENAME = "CASENAME";
	public static final String SELECTED_GENES = "SELECTED_GENES";
	public static String ATTRIBUTES = "ATTRIBUTES";

	@Override
	public int getWizardHeight() {
		
		return 500;
	}
	
	
	@Override
	public int getWizardWidth() {
		
		return 700;
	}
	
	private Map<String, Object> model = new HashMap<String, Object>();
	
	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {

		this.setWindowTitle("Import experiment from TCGA Tests");
		
		List<WizardPageDescriptor> descriptors = Lists.newArrayList();
		
		descriptors.add(createIntroductionPage());
		
		descriptors.add(createMainPage());
	
		descriptors.add(createPage3ForSelecctingSubsetsAndProfiles());
		
		descriptors.add(createPage4ForSelectingGenes());
		
		descriptors.add(createPage5ForSelectingClinicalAtributes());
		
		return descriptors;
	}

	@Override
	protected String getTaskName() {
		return "Import experiment from TCGA test";
	}

	@Override
	protected Experiment backgroundProcess(Monitor monitor) throws Exception {
		FromTCGAExperimentDescriptor descriptor = new FromTCGAExperimentDescriptor(((String[])wizardModel().value(CASENAME))[0], ((String[])wizardModel().value(PROFILE))[0], ((String[])wizardModel().value(STUDY))[0], Arrays.asList(((String[])wizardModel().value(ATTRIBUTES))), Arrays.asList(getids()));
		FromTCGAExperimentFactory experimentFactory = new FromTCGAExperimentFactory(descriptor);
		return experimentFactory.createExperiment();
	}

	@Override
	protected void doInUI(Experiment result) {
		try {
			final Experiment e = result; // join
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					PlatformUIUtils.openEditor(e, ExperimentEditor.id());
//					MessageManager.INSTANCE.openView().add(Message.info("Experiment from file \"" + filePath + "\" was imported sucessfully. Gene Expression lines read: " + e.getNumberOfExpressionLinesInTheOriginalFile() + ". Number of  genes imported: " + e.getNumberOfGenes() + ". Number of collapsed genes: " + e.getNumberOfCollapsedGenes() + " . Collapsing strategy: " + e.getCollapsedStrategyName() + ". "));
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private GenericPage1ForIntroduction createIntroductionPage() {
		
		String inBlankBigTitle =  "Import experiment from TCG Api"; 

		String inBlankSmallTitle =  "Import an experiment, from a text file containing expression data and clinical data";

		String introductionText =  "An experiment includes expression data and clinical data. An experiment in Bioplat can be used for validating Gene Signatures, for optimizing Gene Signatures (feature selection) or just for filtering and visualizing the experiment data in a convenient way. You can also configure a group for each sample, to evaluate if different groups clinical data have got significantly statistic differences.\n\n If your experiment is too large, you should use the last page of this wizard for filtering the genes to import. You can do it pasting the gene list or selecting an opened Gene Signature.\n\nYou can select the collapsing strategy (media, median or variance). It will be used for picking up the gene row if there is more than one row for this gene.";
		
		return new GenericPage1ForIntroduction(inBlankBigTitle, inBlankSmallTitle, introductionText);
	}
	
	private WizardPageDescriptor createMainPage() {
		return new ImportExperimentFromTCGATestPage2(wizardModel());
	}

	private WizardPageDescriptor createPage3ForSelecctingSubsetsAndProfiles() {
		return new ImportExperimentFromTCGATestPage3(wizardModel());
	}

	private WizardPageDescriptor createPage4ForSelectingGenes() {
		return new ImportExperimentFromTCGATestPage4(wizardModel());
	}
	
	private WizardPageDescriptor createPage5ForSelectingClinicalAtributes() {
		return new ImportExperimentFromTCGATestPage5(wizardModel());
	}
	
	@Override
	public boolean logInTheMessageView() {
		return false;
	}
	

	public List<Gene> genes() {
		List<Gene> result = Lists.newArrayList();
		for (String id : getids())
			try {
				result.add(MetaPlat.getInstance().findGene(id));
			} catch (GeneNotFoundByIdException e) {
				MessageManager.INSTANCE.add(Message.warn("Gene '" + id + "' not found"));
			} catch (Exception e) {
				MessageManager.INSTANCE.add(Message.error(e.getLocalizedMessage()));
			}
		return result;
	}
	
	public String[] getids() {
		String genes = wizardModel().value(SELECTED_GENES);
		String value = StringUtils.replace(genes, "\r\n", " ");
//		value = StringUtils.replace(selectedGenes4Filtering, "\r", " ");
//		value = StringUtils.replace(selectedGenes4Filtering, "\n", " ");
		value = StringUtils.replace(value, ",", " ");
		value = StringUtils.replace(value, "\t", " ");
		
		return StringUtils.split(value, " ");
	}
	
	
}