package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.BiomarkerEditor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstracWizard;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.EditedBiomarker;

public class CreateEmptyBiomarkerWizard extends AbstracWizard implements INewWizard {

	private static final String NAME_K = "Name";

	@Override
	protected WizardModel createWizardModel() {
		return new WizardModel().add(NAME_K, new WritableValue("Nuevo biomarcador vacío", String.class));
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		final DataBindingContext dbc = new DataBindingContext();

		addPage(new WizardPage("Configuración") {

			@Override
			public void createControl(Composite parent) {

				WizardPageSupport.create(this, dbc);

				Composite c = new Composite(parent, SWT.NONE);

				new CLabel(c, SWT.BOLD).setText("Nombre:");

				Text nameHolder = new Text(c, SWT.BORDER);

				dbc.bindValue(SWTObservables.observeText(nameHolder, SWT.Modify), model().valueHolder(NAME_K));

				GridLayoutFactory.swtDefaults().numColumns(1).generateLayout(c);
				setControl(c);
			}
		});
	}

	@Override
	public boolean performFinish() {
		Biomarker b = new EditedBiomarker(model().value(NAME_K).toString());
		PlatformUIUtils.openEditor(b, BiomarkerEditor.id());
		return true;
	}

}
