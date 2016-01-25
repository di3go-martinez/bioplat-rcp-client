package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;

@Deprecated
public class CalculateClusterOrUseExistingPage extends WizardPageDescriptor {

	public static final String USE_EXISTING_CLUSTER = "USE_EXISTING_CLUSTER";
	private WizardModel wmodel;
	private Button useExistingCluster;

	public CalculateClusterOrUseExistingPage(String name) {
		super(name);
	}

	@Override
	public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		this.wmodel = wmodel;
		Composite container = Widgets.createDefaultContainer(parent);
		useExistingCluster = new Button(container, SWT.RADIO);
		useExistingCluster.setText("Use sample cluster configured on experiment");

		dbc.bindValue(SWTObservables.observeSelection(useExistingCluster), wmodel.valueHolder(USE_EXISTING_CLUSTER), null, null);

		Button calculateNewCluster = new Button(container, SWT.RADIO);
		calculateNewCluster.setText("Calculate new cluster");

		// dbc.bindValue(SWTObservables.observeSelection(b),
		// wmodel.valueHolder(USE_EXISTING_CLUSTER), new UpdateValueStrategy() {
		// @Override
		// public Object convert(Object value) {
		// return false;
		// }
		// }, null);

		calculateNewCluster.setSelection(true);

		return container;
	}

	@Override
	public boolean allowContinueWizardSetup() {
		//TODO: DavidClustering (No se utiliza esta clase al parecer)
		/*List<AbstractExperiment> selected = wmodel.value(PagesDescriptors.SELECTED);
		boolean okSelection = (selected.size() == 1 && !selected.get(0).getGroups().getClusterDataList().isEmpty());
		useExistingCluster.setEnabled(okSelection);*/

		Boolean b = wmodel.value(USE_EXISTING_CLUSTER);
		return !b;
	}
}
