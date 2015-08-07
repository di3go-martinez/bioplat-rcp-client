package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromCSVFile.FromCSVFilePage2Main;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;

public class ImportExperimentFromTCGATestPage5 extends WizardPageDescriptor {

	public ImportExperimentFromTCGATestPage5(WizardModel wizardModel) {
		super("Get Studios");
	}
	
	@Override
	public Composite create(WizardPage wizardPage, Composite parent,
			DataBindingContext dbc, WizardModel wmodel) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).margins(30, 30).spacing(20, 20).create());
		return container;
	}

	@Override
	public void doOnEnter() {
		// TODO Auto-generated method stub
		super.doOnEnter();
	}
	
	

}
