package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.logRankTest;

import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.ValidationTestGUIProvider;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;



public class LogRankTestGUIProvider extends ValidationTestGUIProvider{



	@Override
	public String getName() {
		
		return "Log rank test";
	}

	@Override
	public void declareVariablesInWizardModel(WizardModel wm) {
		
	}

	@Override
	public void getSpecificParametersForTheValidationTest(
			Map<String, String> result, WizardModel wizardModel) {
		
	}

	@Override
	public boolean isThereAdditionalParameters() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addAdditionComposite(Composite composite, WizardModel wmodel,
			DataBindingContext dbc, GridDataFactory gdf) {
		// TODO Auto-generated method stub
		
	}



	
	
}
