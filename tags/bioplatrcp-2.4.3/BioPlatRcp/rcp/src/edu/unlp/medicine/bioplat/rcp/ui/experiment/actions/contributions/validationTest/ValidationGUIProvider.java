/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.validationTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.tests.SurvCompExecutor;

/**
 * @author Juan
 * 
 */
public class ValidationGUIProvider
		extends
		edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.ValidationTestGUIProvider {

	private static final String SURV_COMP_METHOD = SurvCompExecutor.SURV_COMP_METHOD;

	private static final String CONSERVATIVE = "conservative";
	private static final String NOETHER = "noether";
	private static final String NAME = "name";

	@Override
	public void addAdditionComposite(Composite composite, WizardModel wmodel,
			DataBindingContext dbc, GridDataFactory gdf) {
		List<String> methodsList = Arrays.asList(CONSERVATIVE, NOETHER, NAME);
		ComboViewer methodCombo = Utils.newComboViewer(composite,
				"Concordance index attribute name (see paper Pencina et al.)",
				"Concordance index method name (" + CONSERVATIVE + ", "
						+ NOETHER + " or " + NAME + ")", methodsList);
		methodCombo.setSelection(new StructuredSelection(methodsList.get(0)));
		dbc.bindValue(ViewersObservables.observeSingleSelection(methodCombo),
				wmodel.valueHolder(SURV_COMP_METHOD),
				UpdateStrategies.nonNull("Attribute Name"),
				UpdateStrategies.nullStrategy());
		methodCombo.getCombo().setLayoutData(gdf.create());

		methodCombo.setSelection(new StructuredSelection(NOETHER));
	}

	@Override
	public String getName() {
		return "Test";
	}

	@Override
	public void declareVariablesInWizardModel(WizardModel wm) {
		wm.add(SURV_COMP_METHOD);

	}

	@Override
	public void getSpecificParametersForTheValidationTest(
			Map<String, String> result, WizardModel wizardModel) {
		String survCompMethod = wizardModel.value(SURV_COMP_METHOD);
		result.put(SURV_COMP_METHOD, survCompMethod);

	}

	@Override
	public boolean isThereAdditionalParameters() {
		return true;
	}

}
