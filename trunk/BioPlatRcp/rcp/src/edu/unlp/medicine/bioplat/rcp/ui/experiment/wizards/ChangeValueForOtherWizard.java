package edu.unlp.medicine.bioplat.rcp.ui.experiment.wizards;

import java.awt.TextField;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.logarithms.ENUM_LOGARITHM_BASE;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.logarithms.LogarithmToExpressionLevelsCommand;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.utils.monitor.Monitor;

public class ChangeValueForOtherWizard extends AbstractWizard<Void> {

//	private double oldValue=0.0;
//	private double newValue=0.0;
	private String oldValue;
	private String newValue;
	private AbstractExperiment experiment;

	public ChangeValueForOtherWizard(AbstractExperiment abstractExperiment) {
		this.experiment = abstractExperiment;
		this.setWindowTitle("Apply logarithm on expression data");
	}

	@Override
	protected WizardModel createWizardModel() {
		WizardModel wm = super.createWizardModel();
		wm.add("oldValue", new WritableValue("", String.class));
		wm.add("newValue", new WritableValue("", String.class));
		return wm;

	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();
		WizardPageDescriptor wpd = new WizardPageDescriptor("Change a value for other value in the hole experiment") {
			@Override
			public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				Composite c = Widgets.createDefaultContainer(parent);
				wizardPage.setDescription("It will change one value for other in the experiment expression data.");
				
				GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(c);
				new CLabel(c, SWT.BOLD).setText("");new CLabel(c, SWT.BOLD).setText("");
				
				new CLabel(c, SWT.BOLD).setText("old value:");
				Text oldValueHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(oldValueHolder, SWT.Modify), wizardModel().valueHolder("oldValue"));
				oldValueHolder.setLayoutData(new GridData(100, 13));
				
				new CLabel(c, SWT.BOLD).setText("new value:");
				Text newValueHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(newValueHolder, SWT.Modify), wizardModel().valueHolder("newValue"));
				newValueHolder.setLayoutData(new GridData(100, 13));
				
				return c;
			}
		};
		
		result.add(wpd);
		return result;
	}

	@Override
	protected String getTaskName() {
		return "Change a value for another in expression data...";
	}

	@Override
	protected Void backgroundProcess(Monitor monitor) throws Exception {

		try {
			for (Sample s : experiment.getSamples())
				for (Gene g : experiment.getGenes()) {
					Double expr = experiment.getExpressionLevelForAGene(s, g);
					if (expr == null || expr == Double.parseDouble(oldValue)) {
						experiment.setExpressionLevelForAGene(s, g, Double.parseDouble(newValue));
						addModification(s, g, Double.parseDouble(oldValue), Double.parseDouble(newValue));
				}
			}
		} catch (Exception e) {
			MessageManager.INSTANCE.add(Message.info("No expression data changed"));
		}
		
		try {
			for(Sample s : experiment.getSamples()){
				for(String clinicalAttribute : experiment.getClinicalAttributeNames()){
					if (experiment.getClinicalAttribute(s.getName(), clinicalAttribute).equalsIgnoreCase(oldValue)) {
						experiment.setClinicalAttribute(s.getName(), clinicalAttribute, newValue);
						experiment.getClinicalAttribute(s.getName(), clinicalAttribute);
						MessageManager.INSTANCE.add(Message.info("The clinical attribute " +  oldValue + " in the sample " + s + " has changed. Old value: " + oldValue + ". New value: "+ newValue));
					}
				}
			}
		} catch (Exception e) {
			//No deberia suceder...
		}
		return null;

	}

	private ENUM_LOGARITHM_BASE lbase;

	@Override
	protected void configureParameters() {
		oldValue = (String)wizardModel().valueHolder("oldValue").getValue();
		newValue = (String)wizardModel().valueHolder("newValue").getValue();
	}

	@Override
	protected void doInUI(Void result) throws Exception {

	}

	
	// TODO Generalizar
	protected double getValueToSearch() {
		return ((Double)wizardModel().valueHolder("oldValue").getValue());
	}

	private void addModification(Sample s, Gene g, Double oldValue, Double newValue) {
		MessageManager.INSTANCE.add(Message.info("The expression data of the gene " + g + " in the sample" + s + " has changed. Old value: " + oldValue + ". New value: " + newValue));
	}

	
}
