package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.io.File;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.FileText;
import edu.unlp.medicine.bioplat.rcp.widgets.TextWithSelectionButton;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.exportExperimentCommand.ExportExperimentCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.rAPI.RValidationResult;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.utils.monitor.Monitor;


//TODO: DavidClustering (Se utiliza esta clase?)
public class MevWizard extends AbstractWizard<Void> {
	private static final String INCLUDE_EXPRESSION_DATA = "INCLUDE_EXPRESSION_DATA";
	private static final String INCLUDE_HEADER = "INCLUDE_HEADER";
	private static final String INCLUDE_CLINICAL_DATA = "INCLUDE_CLINICAL_DATA";
	private static final String INCLUDE_CLUSTER = "INCLUDE_CLUSTER";
	private static final String FILE_NAME = "FILE_NAME";

	private AbstractExperiment experiment;
	boolean isExperimentValidation;
	boolean itUsesManualPredefinedCluster;
	private Validation validation;
	
	
	public MevWizard(AbstractExperiment e, boolean isExperimentValidation, boolean itUsesManualPredefinedCluster, Validation validation) {
		this.experiment = e;
		this.validation = validation;
		this.setWindowTitle("Export");
		this.isExperimentValidation = isExperimentValidation;
		this.itUsesManualPredefinedCluster = itUsesManualPredefinedCluster;
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();
		
		
		WizardPageDescriptor d = new WizardPageDescriptor("Export experiment to text file") {

			@Override
			public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				
				wizardPage.setTitle("Export to text file, the experiment data used for this validation");
				
				Composite container = new Composite(parent, SWT.NONE);
				container.setLayout(GridLayoutFactory.fillDefaults().spacing(0, 0).margins(5, 3).create());
				container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
				
				wizardPage.setDescription("Export to text file the experiment data. It will export the expression data of genes contained in this biomarker and the clinical data");

				//GUIUtils.addWrappedText(container, "\n", 8, false);
				
				new Label(container, SWT.NONE).setText("File: ");
				TextWithSelectionButton ft = new FileText(container);
				dbc.bindValue(SWTObservables.observeText(ft.textControl(), SWT.Modify), wmodel.valueHolder(FILE_NAME));

				Button check = new Button(container, SWT.CHECK);
				check.setText("Include Clinical Data");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(INCLUDE_CLINICAL_DATA));

				check = new Button(container, SWT.CHECK);
				check.setText("Include Header");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(INCLUDE_HEADER));

				check = new Button(container, SWT.CHECK);
				check.setText("Include Expression Data");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(INCLUDE_EXPRESSION_DATA));

				check = new Button(container, SWT.CHECK);
				check.setText("Include Cluster");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(INCLUDE_CLUSTER));

				
				GridLayoutFactory.fillDefaults().numColumns(1).applyTo(container);
				
				container.layout(true);
				return container;
			}
		};
		
		result.add(d);
		return result;
	}


	@Override
	protected Void backgroundProcess(Monitor monitor) throws Exception {
		//TODO: DavidClustering
		ExportExperimentCommand command = new ExportExperimentCommand(experiment, filename, includeClinicalData, includeHeader, includeExpressionData, includeCluster,'\t', "\t", isExperimentValidation, itUsesManualPredefinedCluster, validation);
		command.execute();
		return null;
	}

	private String filename;
	private Boolean includeClinicalData, includeHeader, includeExpressionData, includeCluster;

	@Override
	protected String getTaskName() {
		return "Export Gene Signature data matrix used for validation";
	}

	@Override
	protected void doInUI(Void result) throws Exception {
		PlatformUIUtils.openInformation("Export gene signature data matrix to file", "The gene signature data matrix was succesfully exported to: " + filename);
	}

	
	@Override
	protected void configureParameters() {
		final WizardModel m = wizardModel();
		filename = m.value(FILE_NAME);
		includeClinicalData = m.value(INCLUDE_CLINICAL_DATA);
		includeHeader = m.value(INCLUDE_HEADER);
		includeExpressionData = m.value(INCLUDE_EXPRESSION_DATA);
		includeCluster = m.value(INCLUDE_CLUSTER);
	}

	@Override
	protected WizardModel createWizardModel() {
		return new WizardModel()//
				.add(FILE_NAME, new WritableValue("", File.class))//
				.add(INCLUDE_CLINICAL_DATA, new WritableValue(true, Boolean.class))//
				.add(INCLUDE_HEADER, new WritableValue(true, Boolean.class))//
				.add(INCLUDE_EXPRESSION_DATA, new WritableValue(true, Boolean.class))
				.add(INCLUDE_CLUSTER, new WritableValue(true, Boolean.class));
	}
}
