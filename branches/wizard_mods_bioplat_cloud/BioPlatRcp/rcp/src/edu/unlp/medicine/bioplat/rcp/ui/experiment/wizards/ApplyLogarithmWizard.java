package edu.unlp.medicine.bioplat.rcp.ui.experiment.wizards;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.logarithms.ENUM_LOGARITHM_BASE;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.logarithms.LogarithmToExpressionLevelsCommand;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.utils.monitor.Monitor;

public class ApplyLogarithmWizard extends AbstractWizard<Void> {

	private static final String BASE = "BASE";
	private Experiment experiment;

	public ApplyLogarithmWizard(Experiment experiment) {
		this.experiment = experiment;
		this.setWindowTitle("Apply logarithm on expression data");
		
	}

	@Override
	protected WizardModel createWizardModel() {
		WizardModel wm = super.createWizardModel();
		wm.add(BASE, new WritableValue(ENUM_LOGARITHM_BASE.NATURAL, ENUM_LOGARITHM_BASE.class));
		return wm;
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();
		WizardPageDescriptor wpd = new WizardPageDescriptor("Logarithm base") {
			@Override
			public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				Composite c = Widgets.createDefaultContainer(parent);
				wizardPage.setDescription("It will apply logarithm on each expression data, using the logarithm base you have selected.");
				
				GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(c);
				new CLabel(c, SWT.BOLD).setText("");new CLabel(c, SWT.BOLD).setText("");
				
				ComboViewer cv = Utils.newComboViewer(c, "Logarithm base", Arrays.asList(ENUM_LOGARITHM_BASE.values()));
				dbc.bindValue(ViewersObservables.observeSingleSelection(cv), wmodel.valueHolder(BASE), UpdateStrategies.nonNull("Base"), UpdateStrategies.nullStrategy());

				return c;
			}
		};
		
		result.add(wpd);
		return result;
	}

	@Override
	protected String getTaskName() {
		return "Applying Logarithm...";
	}

	@Override
	protected Void backgroundProcess(Monitor monitor) throws Exception {
		new LogarithmToExpressionLevelsCommand(experiment, lbase).execute();
		return null;
	}

	private ENUM_LOGARITHM_BASE lbase;

	@Override
	protected void configureParameters() {
		lbase = wizardModel().value(BASE);
	}

	@Override
	protected void doInUI(Void result) throws Exception {

	}

}
