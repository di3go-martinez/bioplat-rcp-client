package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.ImmutableList;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.ValidationConfigWizard;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.ValidationConfig;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * @author Diego Martínez
 */
public class ValidationConfigPageDescriptor extends WizardPageDescriptor {

	protected Biomarker biomarker;
	private String wizardModelKey;

	/**
	 * 
	 * @param biomarker
	 * @param title
	 * @param wmodelKey
	 *            clave por la cual se accedera al validationconfig en el
	 *            wizardmodel
	 */
	public ValidationConfigPageDescriptor(Biomarker biomarker, String title, String wmodelKey) {
		super(title + " Configuration");
		this.biomarker = biomarker;
		this.wizardModelKey = wmodelKey;
	}

	private Composite innerContainer;
	// innercomposite's content
	private Widget experimentName;
	private Widget numberOfClusters;

	private Widget numberOfTimesToRepeatTheCluster;

	@Override
	public Composite create(final WizardPage wizardPage, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {

		Composite container = Widgets.createDefaultContainer(parent);

		Button b = new Button(container, SWT.FLAT);
		b.setText("Configure Validation Config");
		b.addSelectionListener(new SelectionAdapter() {

			private boolean contentsCreated;

			@Override
			public void widgetSelected(SelectionEvent e) {
				ValidationConfigWizard vcw = new ValidationConfigWizard(biomarker) {
					@Override
					protected void register(ValidationConfig validationConfig) {
						update(validationConfig);
					}
				};
				vcw.open();
			}

			private void update(ValidationConfig config) {

				if (!contentsCreated) {
					experimentName = Widgets.createTextWithLabel(innerContainer, "Experiment name", config, "experimentToValidate.name").readOnly();
					numberOfClusters = Widgets.createTextWithLabel(innerContainer, "Clusters", config, "numberOfClusters").readOnly();
					numberOfTimesToRepeatTheCluster = Widgets.createTextWithLabel(innerContainer, "Times", config, "numberOfTimesToRepeatTheCluster").readOnly();

					innerContainer.layout();
					contentsCreated = true;
				}

				Widgets.retarget(config, experimentName, numberOfClusters, numberOfTimesToRepeatTheCluster);

				wmodel.set(wizardModelKey, ImmutableList.of(config));
				// TODO revisar mejor: actualizo los botones del wizard
				// explícitamente, porque cambio el estado con la asignación del
				// model ...
				wizardPage.getWizard().getContainer().updateButtons();

			}
		});

		innerContainer = Widgets.createDefaultContainer(container, 2);

		return container;
	}

	@Override
	public boolean isPageComplete(WizardModel model) {
		return model.value(wizardModelKey) != null;
	}
}