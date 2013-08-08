package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.survComp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.ValidationTestGUIProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.tests.SurvCompExecutor;



public class SurvCompGUIProvider extends ValidationTestGUIProvider {

	
	public static String SURV_COMP_METHOD = SurvCompExecutor.SURV_COMP_METHOD; 
	
	public static String CONSERVATIVE = "conservative";
	public static String NOETHER = "noether";
	public static String NAME = "name";
	
	@Override
	public void addAdditionComposite(Composite parent,WizardModel wmodel,DataBindingContext dbc, GridDataFactory gdf) {

		

		List methodsList = Arrays.asList(CONSERVATIVE,NOETHER,NAME);		
		ComboViewer methodCombo = Utils.newComboViewer(parent, "Concordance index attribute name (see paper Pencina et al.)", "Concordance index method name (" + CONSERVATIVE + ", " + NOETHER + " or " + NAME+ ")", methodsList);
		methodCombo.setSelection(new StructuredSelection(methodsList.get(0)));
		dbc.bindValue(ViewersObservables.observeSingleSelection(methodCombo), wmodel.valueHolder(SURV_COMP_METHOD), UpdateStrategies.nonNull("Attribute Name"), UpdateStrategies.nullStrategy());
		methodCombo.getCombo().setLayoutData(gdf.create());
		
		methodCombo.setSelection(new StructuredSelection(NOETHER));
		
//		String text = "\n\n\nSee paper Pencina et al. (Pencina, M. J. and D'Agostino, R. B. (2004) \"Overall C as a measure of discrimination in survival analysis: model specific population value and confidence interval estimation\", Statistics in Medicine, 23, pages 2109–2123, 2004.)";
//		GUIUtils.addWrappedText(parent, text , 8, true);

		
	}
	
	
	public String getName(){
		return "Concordance index";
	}




	@Override
	public void declareVariablesInWizardModel(WizardModel wm) {
		wm.add(SURV_COMP_METHOD);
		
	}


	@Override
	public void getSpecificParametersForTheValidationTest(Map<String, String> result, WizardModel wm) {
		String survCompMethod = wm.value(SURV_COMP_METHOD);
		result.put(SURV_COMP_METHOD, survCompMethod);
		
	}


	@Override
	public boolean isThereAdditionalParameters() {

		return true;
	}


}
