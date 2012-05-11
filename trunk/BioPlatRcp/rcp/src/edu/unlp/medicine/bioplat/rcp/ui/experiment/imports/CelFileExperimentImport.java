package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import java.io.File;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.FileText;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.utils.monitor.Monitor;

public class CelFileExperimentImport extends AbstractWizard<Void> {
	private static final String FILE_NAME = "FILE_NAME";
	private static final String RMA = "RMA";
	private static final String FRMA = "FRMA";

	private AbstractExperiment experiment;
	private String filename;
	private Boolean rma, frma;

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();
		WizardPageDescriptor d = new WizardPageDescriptor("Import") {

			@Override
			public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				Composite container = new Composite(parent, SWT.NONE);

				new Label(container, SWT.NONE).setText("File: ");
				FileText ft = new FileText(container, SWT.NONE);
				dbc.bindValue(SWTObservables.observeText(ft.textControl(), SWT.Modify), wmodel.valueHolder(FILE_NAME));

				Button check = new Button(container, SWT.RADIO);
				check.setText("FRMA");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(FRMA));

				check = new Button(container, SWT.RADIO);
				check.setText("RMA");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(RMA));

				GridLayoutFactory.fillDefaults().numColumns(2).applyTo(container);
				return container;
			}
		};
		result.add(d);
		return result;
	}

	@Override
	protected String getTaskName() {
		return "CelFile";
	}

	@Override
	protected Void backgroundProcess(Monitor monitor) throws Exception {
		// ExportExperimentCommand command = new
		// ExportExperimentCommand(experiment, filename, includeClinicalData,
		// includeHeader, includeExpressionData, '\t', "\t");
		// command.execute();
		return null;
	}

	@Override
	protected void doInUI(Void result) throws Exception {

	}

	@Override
	protected void configureParameters() {
		final WizardModel m = wizardModel();
		filename = m.value(FILE_NAME);
		rma = m.value(RMA);
		frma = m.value(FRMA);
	}

	@Override
	protected WizardModel createWizardModel() {
		return new WizardModel()//
				.add(FILE_NAME, new WritableValue("", File.class))//
				.add(RMA, new WritableValue(false, Boolean.class))//
				.add(FRMA, new WritableValue(true, Boolean.class))//
		;
	}

}
