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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.RequiredValidator;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.FileText;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.save.MetaSignatureMarshaller;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.utils.monitor.Monitor;

public class BPLFileBiomarkerImport extends AbstractWizard<Biomarker> {
	/**
	 * Logger Object
	 */
	private static Logger logger = LoggerFactory.getLogger(CelFileExperimentImport.class);

	private static final String FILE_NAME = "FILE_NAME";

	private String fileName;
	private Biomarker biomarker;
	private MetaSignatureMarshaller marshaller = new MetaSignatureMarshaller();

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
				filters.put("*.bpl", "BPL File");
				ft.setFilter(filters);
				dbc.bindValue(SWTObservables.observeText(ft.textControl(), SWT.Modify), wmodel.valueHolder(FILE_NAME), new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("File")), null);
				return container;
			}
		};
		result.add(d);
		return result;
	}

	@Override
	protected String getTaskName() {
		return "BPLFile";
	}

	@Override
	protected Biomarker backgroundProcess(Monitor monitor) throws Exception {
		try {
			biomarker = this.marshaller.unmarshal(this.fileName);
		} catch (Exception e) {
			logger.error("Biomarker Building Exception:", e);
		}

		return biomarker;
	}

	@Override
	protected void doInUI(Biomarker result) throws Exception {
		PlatformUIUtils.openEditor(result, EditorsId.biomarkerEditorId());
	}

	@Override
	protected void configureParameters() {
		final WizardModel m = wizardModel();
		fileName = m.value(FILE_NAME);
	}

	@Override
	protected WizardModel createWizardModel() {
		return new WizardModel()//
				.add(FILE_NAME, new WritableValue("", File.class));
	}

}
