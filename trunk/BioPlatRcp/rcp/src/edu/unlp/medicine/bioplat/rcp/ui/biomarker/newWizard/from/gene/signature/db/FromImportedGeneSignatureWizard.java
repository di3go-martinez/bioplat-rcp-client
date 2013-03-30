package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.from.gene.signature.db;

import java.util.List;

import org.eclipse.ui.INewWizard;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.commandsForGettingNewBiomarker.NewBiomarkerAsACopyGeneSignatureImportedFromExternalDatabase;
import edu.unlp.medicine.utils.monitor.Monitor;

/**
 * 
 * @author diego martínez
 * 
 */
public class FromImportedGeneSignatureWizard extends AbstractWizard<Biomarker> implements INewWizard {

	public FromImportedGeneSignatureWizard() {
		this.setWindowTitle("Copy Gene Signature from external database (GeneSigDB, MolSigDB)");
	}

	
	@Override
	public int getWizardWidth() {
		return 570;
		
	}
	
	
	@Override
	public int getWizardHeight() {
		return 400;
		
	}
	
	
	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();

		result.add(new CopyFromExternalDatabasesWizard(wizardModel()));

		return result;
	}

	@Override
	protected String getTaskName() {
		return " Copy of " + geneSignatureNameOrId + " from  "+ database + " ";
	}

	@Override
	protected Biomarker backgroundProcess(Monitor monitor) throws Exception {
		return new NewBiomarkerAsACopyGeneSignatureImportedFromExternalDatabase(database, geneSignatureNameOrId).execute();
	}

	@Override
	protected void doInUI(Biomarker result) throws Exception {
		PlatformUIUtils.openEditor(result, EditorsId.biomarkerEditorId());
	}

	private String database;
	private String geneSignatureNameOrId;

	@Override
	protected void configureParameters() {
		database = wizardModel().value(CopyFromExternalDatabasesWizard.DATABASE);
		geneSignatureNameOrId = wizardModel().value(CopyFromExternalDatabasesWizard.GENE_SIGNATURE_OR_ID);

	}

	
	
}
