package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IExportWizard;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.utils.monitor.Monitor;

public class ExportToPdfWizard extends AbstractWizard<Void> implements IExportWizard {

	private Biomarker biomarker;

	public ExportToPdfWizard(Biomarker biomarker) {
		this.biomarker = biomarker;
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();
		result.add(new WizardPageDescriptor("Configuration") {

			@Override
			public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				// Create text field
				return parent;
			}
		});
		return result;
	}

	@Override
	protected String getTaskName() {
		return "Exporting to PDF";
	}

	@Override
	protected Void backgroundProcess(Monitor monitor) throws Exception {

		// generate the pdf
		return null;
	}

	@Override
	protected void doInUI(Void result) throws Exception {
		// message
	}

	@Override
	protected WizardModel createWizardModel() {
		// TODO Auto-generated method stub
		return super.createWizardModel();
	}

	@Override
	protected void configureParameters() {
		// hay que acomodar la variable que se le configura
		super.configureParameters();
	}

}
