package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;

public abstract class WizardPageDescriptor {

	private String name;

	public WizardPageDescriptor(String name) {
		this.name = name;
	}

	public String getPageName() {
		return name;
	};

	public abstract Control create(Composite parent, DataBindingContext dbc, WizardModel wmodel);
}
