package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;

public abstract class AbstractWizard extends Wizard implements IWorkbenchWizard {

	private WizardModel model = createWizardModel();

	protected WizardModel model() {
		return model;
	}

	protected abstract WizardModel createWizardModel();

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		final DataBindingContext dbc = new DataBindingContext();

		for (final WizardPageDescriptor pd : createPagesDescriptors()) {
			addPage(new WizardPage(pd.getPageName()) {

				@Override
				public void createControl(Composite parent) {
					WizardPageSupport.create(this, dbc);
					Control control = pd.create(parent, dbc, model());
					setControl(control);
				}
			});
		}
	}

	protected abstract List<WizardPageDescriptor> createPagesDescriptors();

}
