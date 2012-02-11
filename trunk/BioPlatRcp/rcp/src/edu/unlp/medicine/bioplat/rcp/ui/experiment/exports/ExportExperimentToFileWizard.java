package edu.unlp.medicine.bioplat.rcp.ui.experiment.exports;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import edu.unlp.medicine.domainLogic.ext.experimentCommands.exportExperimentCommand.ExportExperimentCommand;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.r4j.constants.OSDependentConstants;

public class ExportExperimentToFileWizard extends Wizard implements IExportWizard {

	Experiment experiment;
	
	@Override
	public void init(IWorkbench arg0, IStructuredSelection selection) {
	 experiment= (Experiment)selection.getFirstElement();
	 
	 addPage(createWizardPage());
	}

	private class WizardModel {
	
		IObservableValue includeClinicalData = new WritableValue(false, Boolean.class);
		IObservableValue includeExpressionData = new WritableValue(false, Boolean.class);
		IObservableValue includeHeader = new WritableValue(false, Boolean.class);

		IObservableValue filename = new WritableValue("experiment", String.class);
		IObservableValue numericSeparator = new WritableValue(".", String.class);
		IObservableValue columnSeparator = new WritableValue("\t", String.class);
		
		IObservableValue directory = new WritableValue(null, String.class);
		{
		
			directory.addChangeListener(new IChangeListener() {

				@Override
				public void handleChange(ChangeEvent event) {
					//FIXME setear el dato
					//parameters.put(ExportGenesToCSVFileCommand.PATH_OF_THE_OUTPUT_FILE, directory.getValue().toString());
				}
			});
			directory.setValue("c:/tmp/biomarker");
		}
	}

	private WizardModel model = new WizardModel();

	private WizardModel getModel() {
		return model;
	}
	private WizardPage createWizardPage() {
		return new WizardPage("Datos") {
			
			@Override
			public void createControl(Composite parent) {

				DataBindingContext dbc = new DataBindingContext();
				WizardPageSupport.create(this, dbc);

				Composite c = new Composite(parent, SWT.BORDER);

				
				new CLabel(c, SWT.BOLD).setText("Nombre de archivo:");
				Text filenameHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(filenameHolder, SWT.Modify), getModel().filename);

				new CLabel(c, SWT.BOLD).setText("Carpeta:");
				Text directoryHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(directoryHolder, SWT.Modify), getModel().directory);

				
				//checkboxes
				new CLabel(c, SWT.BOLD).setText("Incluir datos de expresión");
				Button exprData_cbholder= new Button(c, SWT.CHECK); 
				dbc.bindValue(SWTObservables.observeSelection( exprData_cbholder), getModel().includeExpressionData);
				
				//checkboxes
				new CLabel(c, SWT.BOLD).setText("Incluir nombres de campos");
				Button headerData_cbholder= new Button(c, SWT.CHECK); 
				dbc.bindValue(SWTObservables.observeSelection( headerData_cbholder), getModel().includeHeader);

				//checkboxes
				new CLabel(c, SWT.BOLD).setText("Incluir datos clínicos");
				Button clinicalData_cbholder= new Button(c, SWT.CHECK); 
				dbc.bindValue(SWTObservables.observeSelection( clinicalData_cbholder), getModel().includeClinicalData);

				new CLabel(c, SWT.BOLD).setText("Separador de número:");
				Text numericSeparatorHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(numericSeparatorHolder, SWT.Modify), getModel().numericSeparator);

				new CLabel(c, SWT.BOLD).setText("Separador de columnas:");
				Text columnSeparatorHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(columnSeparatorHolder, SWT.Modify), getModel().columnSeparator);
				
				GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(c);
				setControl(c);
				
			}
		};
	}

	@Override
	public boolean performFinish() {
		String path = this.getModel().directory.getValue().toString() +OSDependentConstants.FILE_SEPARATOR+this.getModel().filename.getValue().toString();
		
		
		new ExportExperimentCommand(experiment,path, (Boolean)this.getModel().includeClinicalData.getValue(), (Boolean)this.getModel().includeHeader.getValue(), (Boolean)this.getModel().includeExpressionData.getValue(), this.getModel().numericSeparator.getValue().toString().charAt(0), this.getModel().columnSeparator.getValue().toString()).execute();
		return true;
	}
	

}
