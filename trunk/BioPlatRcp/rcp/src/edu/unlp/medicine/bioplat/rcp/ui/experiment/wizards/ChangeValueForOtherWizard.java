package edu.unlp.medicine.bioplat.rcp.ui.experiment.wizards;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.logarithms.ENUM_LOGARITHM_BASE;
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
	private Button[] choice = null;
	private boolean isClinicalData = true;
	private String clinicalAttribute;
	private boolean clinicalDataEnabled = true;
	private Combo combo = null;
	private AbstractExperiment experiment;

	public ChangeValueForOtherWizard(AbstractExperiment abstractExperiment) {
		this.experiment = abstractExperiment;
		this.setWindowTitle("Change value for another value");
	}

	@Override
	protected WizardModel createWizardModel() {
		WizardModel wm = super.createWizardModel();
		wm.add("oldValue", new WritableValue("", String.class));
		wm.add("newValue", new WritableValue("", String.class));
		return wm;

	}	

	@Override
		public boolean canFinish() {
		
		return true;
		//return (!((String)wizardModel().valueHolder("oldValue").getValue()).equals("")) && !((String)wizardModel().valueHolder("newValue").getValue()).equals("");
		
		}
	
	@Override
	public int getWizardWidth() {

		return 700;
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
				
				choice = new Button[2];

				clinicalDataEnabled = experiment.getClinicalAttributeNames().size() > 0;

				choice[0] = new Button(c, SWT.RADIO);
				choice[0].setSelection(true);
				choice[0].setText("Replace clinical data");
				choice[0].addSelectionListener(generateSelectionListener());
				choice[0].setEnabled(clinicalDataEnabled);

				
				choice[1] = new Button(c, SWT.RADIO);
				//choice[1].setSelection(true);
				choice[1].setText("Replace expression data");
				choice[1].addSelectionListener(generateSelectionListener());
				
				
				
				new CLabel(c, SWT.BOLD).setText("Select the attribute to replace: ");
				combo = new Combo(c, SWT.READ_ONLY);
				List<String> clinicalAttNames = experiment.getClinicalAttributeNames();
				Collections.sort(clinicalAttNames);
				combo.setItems(clinicalAttNames.toArray(new String[0]));
				combo.setEnabled(true);
				if (combo.getItemCount()>0) {
					combo.select(0);
					clinicalAttribute=combo.getItem(0);
				}
				
				combo.addSelectionListener(new SelectionAdapter() {
					 public void widgetSelected(SelectionEvent e) {
						 clinicalAttribute = combo.getText();
						 
					 }
				});
				
				return c;
			}
		};
		
		result.add(wpd);
		return result;
	}

	
	
	@Override
	protected String getTaskName() {
		return "Change a value for another...";
	}

	@Override
	protected Void backgroundProcess(Monitor monitor) throws Exception {
		
		long totalvaluesChanged = 0L;
		if (isClinicalData){
			try {
				for(Sample s : experiment.getSamples()){
//					for(String clinicalAttribute : experiment.getClinicalAttributeNames()){
						String actualValue = experiment.getClinicalAttribute(s.getName(), clinicalAttribute);
						if ( (actualValue!=null && actualValue.equalsIgnoreCase(oldValue)) || (actualValue==null && oldValue.equals(""))){
							experiment.setClinicalAttribute(s.getName(), clinicalAttribute, newValue);
							totalvaluesChanged++;
							MessageManager.INSTANCE.add(Message.info("The clinical attribute " +  oldValue + " in the sample " + s + " has changed. Old value: " + oldValue + ". New value: "+ newValue));
						}
//					}
				}
			} catch (Exception e) {
				MessageManager.INSTANCE.add(Message.info("No expression data changed"));
			} 
		} else {
			try {
				for (Sample s : experiment.getSamples())
					for (Gene g : experiment.getGenes()) {
						Double expr = experiment.getExpressionLevelForAGene(s, g);
						if (expr == null || expr.equals(Double.parseDouble(oldValue))) {
							experiment.setExpressionLevelForAGene(s, g, Double.parseDouble(newValue));
							addModification(s, g, Double.parseDouble(oldValue), Double.parseDouble(newValue));
							totalvaluesChanged++;
						}
					}
			} catch (Exception e) {
				MessageManager.INSTANCE.add(Message.info("No expression data changed"));
			}
		}
		PlatformUIUtils.openInformation("Finished replacing values", totalvaluesChanged+" values changed. Old value: "+oldValue+". New value: "+newValue);
		
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
	
	private SelectionListener generateSelectionListener(){
		SelectionListener selectionListener = new SelectionAdapter () {
	         public void widgetSelected(SelectionEvent event) {
	            Button button = ((Button) event.widget);
	            isClinicalData = button.getText().equals("Replace clinical data");
	            combo.setEnabled(isClinicalData);
	         };
	      };
	    return selectionListener;
	}
	
	
}
