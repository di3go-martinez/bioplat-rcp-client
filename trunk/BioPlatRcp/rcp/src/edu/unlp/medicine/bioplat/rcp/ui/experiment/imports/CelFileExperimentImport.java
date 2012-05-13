package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.RequiredValidator;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.FileText;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.ExperimentFromCelFileImporter;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.ENUM_NORMALIZATION_METHOD;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;
import edu.unlp.medicine.utils.monitor.Monitor;

public class CelFileExperimentImport extends AbstractWizard<Void> {
	/**
	 * Logger Object
	 */
	private static Logger logger = LoggerFactory.getLogger(CelFileExperimentImport.class);

	private static final String FILE_NAME = "FILE_NAME";
	private static final String RMA = "RMA";
	private static final String FRMA = "FRMA";

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
				FileText ft = new FileText(container, SWT.BORDER);
				Map<String, String> filters = Maps.newHashMap();
				filters.put("*.cel", "CEL File");
				ft.setFilter(filters);
				dbc.bindValue(SWTObservables.observeText(ft.textControl(), SWT.Modify), wmodel.valueHolder(FILE_NAME), new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("CEL File")), null);

				Button check = new Button(container, SWT.RADIO);
				check.setText("FRMA");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(FRMA));

				check = new Button(container, SWT.RADIO);
				check.setText("RMA");
				dbc.bindValue(SWTObservables.observeSelection(check), wmodel.valueHolder(RMA));

				// GridLayoutFactory.fillDefaults().numColumns(2).applyTo(container);
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
		ExperimentFromCelFileImporter importer = new ExperimentFromCelFileImporter(this.filename, getNormalizationMethod());
		Experiment experiment = null;
		try {
			experiment = importer.execute();
		} catch (ExperimentBuildingException e) {
			logger.error("Experiment Building Exception:", e);
		}

		return null;
	}

	private ENUM_NORMALIZATION_METHOD getNormalizationMethod() {
		ENUM_NORMALIZATION_METHOD method = ENUM_NORMALIZATION_METHOD.FRMA;
		if (this.rma) {
			method = ENUM_NORMALIZATION_METHOD.RMA;
		}
		return method;
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
