package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;

public abstract class ValidationTestGUIProvider {

	
	public abstract void addAdditionComposite(Composite composite, WizardModel wmodel,DataBindingContext dbc, GridDataFactory gdf);

	public abstract String getName();

	public abstract void declareVariablesInWizardModel(WizardModel wm);

	public abstract void getSpecificParametersForTheValidationTest(Map<String, String> result, WizardModel wizardModel);

	public abstract  boolean isThereAdditionalParameters();

		
		
	

	
	
}
