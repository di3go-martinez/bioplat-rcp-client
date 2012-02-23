package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import org.eclipse.jface.wizard.Wizard;

import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;

public abstract class AbstracWizard extends Wizard {

	private WizardModel model = createWizardModel();

	protected WizardModel model() {
		return model;
	}

	protected abstract WizardModel createWizardModel();
}
