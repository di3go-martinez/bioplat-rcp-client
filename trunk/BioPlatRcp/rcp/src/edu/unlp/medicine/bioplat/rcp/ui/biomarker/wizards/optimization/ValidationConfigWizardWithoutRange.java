package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization;

import org.eclipse.core.databinding.observable.value.WritableValue;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.ValidationConfigWizard;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class ValidationConfigWizardWithoutRange extends ValidationConfigWizard {

	public ValidationConfigWizardWithoutRange(Biomarker biomarker) {
		super(biomarker);
	}

	@Override
	protected WritableValue clusterWritableValue() {
		return new WritableValue(2, Integer.class);
	}
	

	@Override
	protected String getClusterRangeAsString() {
		
		Integer cluster = wizardModel().value(PagesDescriptors.NUMBER_OF_CLUSTERS);
		return String.valueOf(cluster);
	}
	
}
