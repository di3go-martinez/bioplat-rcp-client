package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;

import com.google.common.collect.Lists;


import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.EditedBiomarker;
import edu.unlp.medicine.utils.monitor.Monitor;

public class CreateEmptyBiomarkerWizard extends AbstractWizard<Biomarker> implements INewWizard {

	private static final String NAME_K = "Name";

	@Override
	protected WizardModel createWizardModel() {
		return new WizardModel().add(NAME_K, new WritableValue("Nuevo biomarcador vacío", String.class));
	}

	@Override
	protected void configureParamenters() {
		name = model().value(NAME_K).toString();
	}

	private String name = "";

	@Override
	public Biomarker backgroundProcess(Monitor m) {
		return new EditedBiomarker(name);
	}

	@Override
	protected void doInUI(Biomarker result) throws Exception {
		PlatformUIUtils.openEditor(result, EditorsId.biomarkerEditorId());
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();

		result.add(new WizardPageDescriptor("Configuración") {

			@Override
			public Composite create(Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				Composite c = new Composite(parent, SWT.NONE);
				new CLabel(c, SWT.BOLD).setText("Nombre:");
				Text nameHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(nameHolder, SWT.Modify), model().valueHolder(NAME_K));
				GridLayoutFactory.swtDefaults().numColumns(1).margins(5, 5).generateLayout(c);
				return c;
			}
		});

		return result;
	}

	@Override
	protected String getTaskName() {
		return "Creando un biomarcador vacío";
	}

}
