package edu.unlp.medicine.bioplat.rcp.ui.experiment.exports;

import java.io.File;
import java.util.Arrays;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.RequiredValidator;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.widgets.DirectoryText;
import edu.unlp.medicine.bioplat.rcp.widgets.TextWithSelectionButton;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.exportExperimentCommand.ExportExperimentCommand;
import edu.unlp.medicine.domainLogic.framework.constants.Constants;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.r4j.constants.OSDependentConstants;

public class ExportExperimentToFileWizard extends Wizard implements IExportWizard {

	private Experiment experiment;
	final static String TAB = "Tab";
	final static String COMMA = "Comma";
	final static String SPACE = "Space";
	final static String DOT = "Dot";
	

	public ExportExperimentToFileWizard(Experiment e) {
		experiment = e;
	}

	public void init() {
//
//		if (!selection.isEmpty())
//			experiment = (Experiment) selection.getFirstElement();
//
//		else
//			experiment = findExperimentOnCurrentEditor();
//
//		if (experiment == null) {
//			MessageManager.INSTANCE.add(Message.warn("No Experiment selected"));
//		}

		this.setWindowTitle("Export BioPlat experiment to text file");
		addPage(createWizardPage());
	}

	/**
	 * 
	 * @return el experimento del editor actual o null si este no es un
	 *         experimento
	 */
	private Experiment findExperimentOnCurrentEditor() {
		try {
			ModelProvider<Experiment> expeditor = (ModelProvider<Experiment>) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			return expeditor.model();
		} catch (Exception e) {
			return null;
		}
	}

	private class WizardModel {

		IObservableValue includeClinicalData = new WritableValue(false, Boolean.class);
		IObservableValue includeExpressionData = new WritableValue(false, Boolean.class);
		IObservableValue includeHeader = new WritableValue(false, Boolean.class);
		IObservableValue includeCluster = new WritableValue(false, Boolean.class);

		IObservableValue filename = new WritableValue("experiment", String.class);
		IObservableValue numericSeparator = new WritableValue(".", String.class);
		IObservableValue columnSeparator = new WritableValue("\t", String.class);

		IObservableValue directory = new WritableValue(null, String.class);
		{

			directory.addChangeListener(new IChangeListener() {

				@Override
				public void handleChange(ChangeEvent event) {
					// FIXME setear el dato
					// parameters.put(ExportGenesToCSVFileCommand.PATH_OF_THE_OUTPUT_FILE,
					// directory.getValue().toString());
				}
			});
			directory.setValue(".");
		}
	}

	private WizardModel model = new WizardModel();

	private WizardModel getModel() {
		return model;
	}

	private WizardPage createWizardPage() {
		return new WizardPage("Export parameters") {

			@Override
			public void createControl(Composite parent) {

				DataBindingContext dbc = new DataBindingContext();
				WizardPageSupport.create(this, dbc);

				Composite c = new Composite(parent, SWT.NONE);
				c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(50, 100).spacing(2, 50).create());
				
				new Label(c, SWT.NONE).setText("Folder: ");
				TextWithSelectionButton ft = new DirectoryText(c);
				dbc.bindValue(SWTObservables.observeText(ft.textControl(), SWT.Modify), getModel().directory);
				getModel().filename.setValue("");

				new CLabel(c, SWT.BOLD).setText("File name:");
				Text filenameHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(filenameHolder, SWT.Modify), getModel().filename);
				getModel().filename.setValue(experiment.getName());

				ComboViewer numerSeparatorCombo = Utils.newComboViewer(c, "Number separator character:", Arrays.asList(COMMA, DOT));
				IObservableValue widgetObservable4Column = ViewersObservables.observeSingleSelection(numerSeparatorCombo);
				dbc.bindValue(widgetObservable4Column,  getModel().numericSeparator, new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("NumberSeparator")), null);
				getModel().numericSeparator.setValue(COMMA);

				
				ComboViewer separatorCombo = Utils.newComboViewer(c, "Column separator character:", Arrays.asList(TAB, COMMA, SPACE, DOT));
				IObservableValue widgetObservable = ViewersObservables.observeSingleSelection(separatorCombo);
				dbc.bindValue(widgetObservable,  getModel().columnSeparator, new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("Separator")), null);
				getModel().columnSeparator.setValue(TAB);
				
				
				// checkboxes
				new CLabel(c, SWT.BOLD).setText("Include expression data");
				Button exprData_cbholder = new Button(c, SWT.CHECK);
				dbc.bindValue(SWTObservables.observeSelection(exprData_cbholder), getModel().includeExpressionData);

				// checkboxes
				new CLabel(c, SWT.BOLD).setText("Include sample names (header)");
				Button headerData_cbholder = new Button(c, SWT.CHECK);
				dbc.bindValue(SWTObservables.observeSelection(headerData_cbholder), getModel().includeHeader);

				// checkboxes
				new CLabel(c, SWT.BOLD).setText("Include clinical data");
				Button clinicalData_cbholder = new Button(c, SWT.CHECK);
				dbc.bindValue(SWTObservables.observeSelection(clinicalData_cbholder), getModel().includeClinicalData);

				
				
				GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(c);
				setControl(c);
				
				this.setTitle("Export configuration");
				this.setDescription("Select the destination folder and file. You can also parametrize the data to include.");

			}
		};
	}

	@Override
	public boolean performFinish() {
		String absoluteFilename = this.getModel().directory.getValue().toString() + OSDependentConstants.FILE_SEPARATOR + this.getModel().filename.getValue().toString();

		new ExportExperimentCommand(experiment, absoluteFilename, (Boolean) this.getModel().includeClinicalData.getValue(), (Boolean) this.getModel().includeHeader.getValue(), (Boolean) this.getModel().includeExpressionData.getValue(), (Boolean) this.getModel().includeCluster.getValue(), convertSeparator(this.getModel().numericSeparator.getValue().toString()).charAt(0), convertSeparator(this.getModel().columnSeparator.getValue().toString()), true, false).execute();
											
		
		MessageManager.INSTANCE.add(Message.info("The file " + new File(absoluteFilename).getAbsoluteFile() + " was succesfully exported."));
		return true;
	}

	private String convertSeparator(String separator) {
		if (separator.equals(TAB)) return "\t";
		if (separator.equals(COMMA)) return ",";
		if (separator.equals(SPACE)) return " ";
		if (separator.equals(DOT)) return ".";
		return "\t";
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}
}
