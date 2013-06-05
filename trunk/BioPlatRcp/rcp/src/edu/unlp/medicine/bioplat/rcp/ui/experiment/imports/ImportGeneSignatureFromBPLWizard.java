package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
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

public class ImportGeneSignatureFromBPLWizard extends AbstractWizard<Biomarker> {
	/**
	 * Logger Object
	 */
	private static Logger logger = LoggerFactory.getLogger(CelFileExperimentImport.class);

	private static final String FILE_NAME = "FILE_NAME";

	private Biomarker biomarker;
	private String fileName;
	private MetaSignatureMarshaller marshaller = new MetaSignatureMarshaller();

	@Override
	public int getWizardWidth() {

		return 770;
	}

	@Override
	public int getWizardHeight() {

		return 370;
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();
		WizardPageDescriptor d = new WizardPageDescriptor("Select file") {
			@Override
			public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {

				wizardPage.setDescription("When you are working on a gene Signature using Bioplat, you can save it in a .BPL file. Later, you can recover it (with all its information), using this option.");

				GridData gridData = new GridData();
				gridData.horizontalAlignment = SWT.FILL;
				gridData.grabExcessHorizontalSpace = true;

				Composite group = new Group(parent, SWT.NONE);
				group.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(20, 20).spacing(2, 20).create());
				group.setLayoutData(gridData);

				new Label(group, SWT.NONE).setText("BPL File: ");
				FileText ft = new FileText(group, SWT.BORDER);
				Map<String, String> filters = Maps.newHashMap();
				filters.put("*.bpl", "BPL File");
				ft.setFilter(filters);
				dbc.bindValue(SWTObservables.observeText(ft.textControl(), SWT.Modify), wmodel.valueHolder(FILE_NAME), new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("File")), null);
				// dbc.bindValue(SWTObservables.observeText(ft.textControl(),
				// SWT.Modify), wmodel.valueHolder(FILE_NAME), null, null);
				return group;
			}
		};
		result.add(d);
		return result;
	}

	@Override
	protected String getTaskName() {
		return "'Open the gene signature from: " + fileName + " '";
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

	public ImportGeneSignatureFromBPLWizard() {
		this.setWindowTitle("Open Gene Signature from BPL file");

	}

}
