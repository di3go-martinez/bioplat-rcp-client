package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization;

import java.util.ArrayList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.google.common.collect.ImmutableList;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.ValidationConfigWizard;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.LogRankTestCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.LogRankTestValidationConfig;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * @author Diego Martínez
 */
public class ValidationConfigPageDescriptor extends WizardPageDescriptor {

	protected Biomarker biomarker;
	private String wizardModelKey;
	Image image4Button;
	String type;
	String description;
	/**
	 * 
	 * @param biomarker
	 * @param title
	 * @param wmodelKey
	 *            clave por la cual se accedera al validationconfig en el
	 *            wizardmodel
	 */
	public ValidationConfigPageDescriptor(Biomarker biomarker, String type, String description, String wmodelKey, Image image4Button) {
		super("Select the experiment to do "+ type);
		this.biomarker = biomarker;
		this.wizardModelKey = wmodelKey;
		this.image4Button=image4Button;
		this.type = type;
		this.description = description;
	}

	private Composite innerContainer;
	// innercomposite's content
	private Widget experimentName;
	private Widget numberOfClusters;
	private Widget attribtueNameToDoTheValidation;
	private Widget numberOfTimesToRepeatTheCluster;
	private Widget attribtueNameToDoTheValidation2;
	private Widget statisticsSignificanceTest;
	private boolean optional;

	@Override
	public Composite create(final WizardPage wizardPage, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {

		//Composite container = Widgets.createDefaultContainer(parent);
		wizardPage.setDescription(description);

		GridData extensor = new GridData();
		extensor.horizontalAlignment=SWT.CENTER;
		extensor.grabExcessHorizontalSpace=true;
		extensor.verticalIndent=300;
		
		Composite group = new Group(parent, SWT.NONE);
		group.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(50,50).spacing(2, 50).create());
		group.setLayoutData(extensor);

		Button b = new Button(group, SWT.PUSH);
		b.setText("Select " + type + " dataset (Bioplat Experiment in your desktop)");
		b.setImage(image4Button);
		GridData gd = new GridData(400,70);gd.horizontalAlignment = SWT.CENTER;
		b.setLayoutData(gd);
		b.addSelectionListener(new SelectionAdapter() {

			private boolean contentsCreated = false;

			@Override
			public void widgetSelected(SelectionEvent e) {
				ValidationConfigWizard vcw = new ValidationConfigWizard(biomarker, false) {
					@Override
					protected void register(ValidationConfig4DoingCluster validationConfig) {
						update(validationConfig);
					}

					@Override
					public OneBiomarkerCommand createCommand(
							Biomarker aBiomarker,
							ArrayList<ValidationConfig4DoingCluster> validationConfigs) {
						// TODO Auto-generated method stub
						return new LogRankTestCommand(aBiomarker, validationConfigs);
					}
				};

				vcw.open();
			}

			private void update(ValidationConfig4DoingCluster config) {

				if (!contentsCreated) {
					experimentName = Widgets.createTextWithLabel(innerContainer, "Experiment Name", config, "experimentToValidate.name").readOnly();
					numberOfClusters = Widgets.createTextWithLabel(innerContainer, "Clusters", config, "numberOfClusters").readOnly();
					numberOfTimesToRepeatTheCluster = Widgets.createTextWithLabel(innerContainer, "Times", config, "numberOfTimesToRepeatTheCluster").readOnly();
					attribtueNameToDoTheValidation = Widgets.createTextWithLabel(innerContainer, "Validation Attribute 1", config, "attribtueNameToDoTheValidation").readOnly();
					attribtueNameToDoTheValidation2 = Widgets.createTextWithLabel(innerContainer, "Validation Attribute 2", config, "secondAttribtueNameToDoTheValidation").readOnly();
					statisticsSignificanceTest = Widgets.createTextWithLabel(innerContainer, "Statistics Significance Test", config, "statisticsSignificanceTest.friendlyName").readOnly();

					innerContainer.layout();
					contentsCreated = true;
				}

				Widgets.retarget(config, experimentName, numberOfClusters, numberOfTimesToRepeatTheCluster, attribtueNameToDoTheValidation, attribtueNameToDoTheValidation2, statisticsSignificanceTest);

				wmodel.set(wizardModelKey, ImmutableList.of(config));
				// TODO revisar mejor: actualizo los botones del wizard
				// explícitamente, porque cambio el estado con la asignación del
				// model ...
				wizardPage.getWizard().getContainer().updateButtons();

			}
		});

		innerContainer = Widgets.createDefaultContainer(group, 2);

		return group;
	}

	@Override
	public boolean isPageComplete(WizardModel model) {
		return optional || model.value(wizardModelKey) != null;
	}

	public WizardPageDescriptor optional() {
		optional = true;
		return this;
	}

	private boolean disableClusterRanges = false;

	public WizardPageDescriptor disableClusterRange() {
		disableClusterRanges = true;
		return this;
	}
	
		
}