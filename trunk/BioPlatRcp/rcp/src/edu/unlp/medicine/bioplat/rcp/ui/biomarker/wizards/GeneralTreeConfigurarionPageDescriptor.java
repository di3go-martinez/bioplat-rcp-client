package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardPageUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;

public class GeneralTreeConfigurarionPageDescriptor extends WizardPageDescriptor {

	static final String NUM_BIOMARKERS_TO_KEEP = "NUM_BIOMARKERS_TO_KEEP";
	static final String NUMBER_OF_GENES = "NUMBER_OF_GENES";

	public GeneralTreeConfigurarionPageDescriptor() {
		super("Tree");
	}

	@Override
	public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		Composite container = Widgets.createDefaultContainer(parent, 2);

		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(NUM_BIOMARKERS_TO_KEEP), "Number of biomarkers to keep en each round");
		WizardPageUtils.createText(container, dbc, wmodel.valueHolder(NUMBER_OF_GENES), "Minimum number of genes");

		return container;
	}

	GeneralTreeConfigurarionPageDescriptor addParameters(WizardModel wmodel) {
		wmodel.add(NUM_BIOMARKERS_TO_KEEP, Integer.class, 0);
		wmodel.add(NUMBER_OF_GENES, Integer.class, 0);
		return this;
	}

}
