package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import javax.annotation.Nullable;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;

public abstract class WizardPageDescriptor {

	private String name;

	public WizardPageDescriptor(String name) {
		this.name = name;
	}

	public String getPageName() {
		return name;
	};

	public abstract Composite create(@Nullable WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel);

	@Override
	public String toString() {
		return "Wizard page " + getPageName();
	}

	public boolean isPageComplete(WizardModel model) {
		return true;
	}

}
