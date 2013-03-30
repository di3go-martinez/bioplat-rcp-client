package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import java.io.File;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.RequiredValidator;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.DirectoryText;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.ExperimentFromCelFileImporter;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.ENUM_NORMALIZATION_METHOD;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;
import edu.unlp.medicine.utils.monitor.Monitor;

public class CelFileExperimentImport extends AbstractWizard<Experiment> {

	private static Logger logger = LoggerFactory.getLogger(CelFileExperimentImport.class);

	private static final String FOLDER_NAME = "FOLDER_NAME";
	private static final String RMA = "RMA";
	private static final String FRMA = "FRMA";

	private String folderName;
	private Boolean rma, frma;
	private Experiment experiment;

	@Override
	public int getWizardWidth() {
		
		return 670;
	}
	
	
	@Override
	public int getWizardHeight() {
		return 600;
	}
	
	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();
		WizardPageDescriptor d = new WizardPageDescriptor("Import experiment using your .CEL files") {

			@Override
			public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				
				wizardPage.setTitle("Import your .CEL files to Bioplat");
				wizardPage.setDescription("Import and normalize an experiment you have in your .CEL files. They will be useful for doing validation of your biomarkers.");
				
				GridData gridData = new GridData();
				gridData.horizontalAlignment=SWT.FILL;
				gridData.grabExcessHorizontalSpace=true;
				
				//COMPOSITE
				Composite group = new Group(parent, SWT.NONE);
				group.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(20,20).spacing(2, 20).create());
				group.setLayoutData(gridData);

				//LABEL + FOLDER INPUT
				new Label(group, SWT.NONE).setText("Folder in which you have your .CEL files: ");
				Composite directoryComposite = new Composite(group, SWT.NONE);
				DirectoryText ft = new DirectoryText(directoryComposite, SWT.NONE);
				directoryComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
				directoryComposite.setLayoutData(gridData);
				
				
				
				/*
				 * Map<String, String> filters = Maps.newHashMap();
				 * filters.put("*.cel", "CEL File");
				 */
				dbc.bindValue(SWTObservables.observeText(ft.textControl(), SWT.Modify), wmodel.valueHolder(FOLDER_NAME), null, null);

				Composite normalizationGroup = new Group(group, SWT.NONE);
				normalizationGroup.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(5,5).spacing(7, 10).create());
				GridData gridData4RadioButton = new GridData();
				gridData4RadioButton.horizontalAlignment = GridData.CENTER;
				gridData4RadioButton.horizontalSpan = 2;
				normalizationGroup.setLayoutData(gridData4RadioButton);
				
				Button check = new Button(normalizationGroup, SWT.RADIO);
				check.setText("Normalize using FRMA");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(FRMA));

				check = new Button(normalizationGroup, SWT.RADIO);
				check.setText("Normalize using RMA");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(RMA));

				
				
				//Label for explaining the possible 
				Label introdudctionLabel = new Label(group, SWT.WRAP);
				introdudctionLabel.setText("\nNote: A collapse strategy will be applied automatically. The gene will be represented by the probe with the highest average.");
				GridData gridData4Text = new GridData(GridData.FILL_HORIZONTAL); 
				gridData4Text.horizontalSpan = 2;
				introdudctionLabel.setLayoutData(gridData4Text);
				FontData[] fD = introdudctionLabel.getFont().getFontData();
				fD[0].setHeight(8);
				fD[0].setStyle(SWT.ITALIC);
				introdudctionLabel.setFont( new Font(group.getDisplay(),fD[0]));
				

				return group;
			}
		};
		result.add(d);
		return result;
	}

	@Override
	protected String getTaskName() {
		return "Import experiment from .Cel files. Folder:\" " + folderName + "\"";
	}

	@Override
	protected Experiment backgroundProcess(Monitor monitor) throws Exception {
		ExperimentFromCelFileImporter importer = new ExperimentFromCelFileImporter(this.folderName, getNormalizationMethod());
		try {
			experiment = importer.execute();
		} catch (ExperimentBuildingException e) {
			logger.error("Experiment Building Exception:", e);
		}

		return experiment;
	}

	private ENUM_NORMALIZATION_METHOD getNormalizationMethod() {
		ENUM_NORMALIZATION_METHOD method = ENUM_NORMALIZATION_METHOD.FRMA;
		if (this.rma) {
			method = ENUM_NORMALIZATION_METHOD.RMA;
		}
		return method;
	}

	@Override
	protected void doInUI(Experiment result) throws Exception {
		PlatformUIUtils.openEditor(result, EditorsId.experimentEditorId());
	}

	@Override
	protected void configureParameters() {
		final WizardModel m = wizardModel();
		folderName = m.value(FOLDER_NAME);
		rma = m.value(RMA);
		frma = m.value(FRMA);
	}

	@Override
	protected WizardModel createWizardModel() {
		return new WizardModel()//
				.add(FOLDER_NAME, new WritableValue("", File.class))//
				.add(RMA, new WritableValue(false, Boolean.class))//
				.add(FRMA, new WritableValue(true, Boolean.class))//
		;
	}

	
	public CelFileExperimentImport(){
		this.setWindowTitle("Import experiment from .CEL file");
	}
	
	@Override
	public int getMinimumWith(){
		return 900;
	}

	
}
