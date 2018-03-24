package edu.unlp.medicine.bioplat.rcp.config;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.ValidationTestGUIProvider;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.survComp.SurvCompGUIProvider;

public class StatisticConfigGUI {
	
	public static ValidationTestGUIProvider getValidationTestGUIProvider(){
		return new SurvCompGUIProvider();
	}

}
