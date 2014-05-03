package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import edu.unlp.medicine.bioplat.rcp.ui.utils.Models;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.FilePathValidator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.RequiredValidator;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.DirectoryText;
import edu.unlp.medicine.bioplat.rcp.widgets.TextWithSelectionButton;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.ext.experimentCommands.exportExperimentCommand.ExportExperimentCommand;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.ExportGenesToCSVFileCommand;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.r4j.constants.OSDependentConstants;

/**
 * exportador que tiene como parámetros el separador y el nombre completo del
 * archivo a escribir
 * 
 * @author diego
 * 
 */
public class ExportToFileWizard extends Wizard implements IExportWizard {

	Biomarker biomarker;
	
	final static String TAB = "Tab";
	final static String COMMA = "Comma";
	final static String SPACE = "Space";
	final static String DOT = "Dot";
	
	public ExportToFileWizard(Biomarker b) {
		this.biomarker = b;
	}
	
	public ExportToFileWizard() {
		setNeedsProgressMonitor(true);
	}

	private Map<String, String> parameters = new HashMap<String, String>();

	public void myInit() {
//		if (!validate(selection)) {
//			MessageManager.INSTANCE/* .clear() */.add(Message.warn("There is not Gene Signature in your desktop.")).openView();
//			PlatformUIUtils.openInformation("Export gene signature", "There is not Gene Signature in your desktop.");
//			return;
//		}
		addPage(createWizardPage());
	}

	private boolean validate(IStructuredSelection selection) {
//		try {
//			selectedBiomarker = (Biomarker) selection.getFirstElement();
//			if (selectedBiomarker == null)
//				selectedBiomarker = Models.getInstance().getActiveBiomarker();
//			return selectedBiomarker != null;
//		} catch (ClassCastException cce) {
//			// la selección actual no es un biomarcador
//			return false;
//		}
		return true;
	}

	private class WizardModel {
		IObservableValue separator = new WritableValue(".", String.class);
		

		IObservableValue filename = new WritableValue("experiment", String.class);
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

		
//		IObservableValue path = new WritableValue(null, String.class);
//		{
//			separator.addChangeListener(new IChangeListener() {
//
//				@Override
//				public void handleChange(ChangeEvent event) {
//					parameters.put(ExportGenesToCSVFileCommand.SEPARATOR, separator.getValue().toString());
//				}
//			});
//
//			path.addChangeListener(new IChangeListener() {
//
//				@Override
//				public void handleChange(ChangeEvent event) {
//					parameters.put(ExportGenesToCSVFileCommand.PATH_OF_THE_OUTPUT_FILE, path.getValue().toString());
//				}
//			});
//			// le seteo valores por default
//			separator.setValue(",");
//			path.setValue("./geneSignature.exp");
//		}
	}

	private WizardModel model = new WizardModel();

	private WizardModel getModel() {
		return model;
	}

	private WizardPage createWizardPage() {
		return new WizardPage("Export to file") {
			{
				setPageComplete(false);
			}

			@Override
			public void createControl(Composite parent) {

				DataBindingContext dbc = new DataBindingContext();
				WizardPageSupport.create(this, dbc);

				Composite c = new Composite(parent, SWT.NONE);
				c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(50, 100).spacing(2, 50).create());

				//new CLabel(c, SWT.BOLD).setText("Separator:");
				//Text separatorHolder = new Text(c, SWT.BORDER);
				//dbc.bindValue(SWTObservables.observeText(separatorHolder, SWT.Modify), getModel().separator);
				
				new Label(c, SWT.NONE).setText("Folder: ");
				TextWithSelectionButton ft = new DirectoryText(c);
				dbc.bindValue(SWTObservables.observeText(ft.textControl(), SWT.Modify), getModel().directory);
				getModel().filename.setValue("");

				new CLabel(c, SWT.BOLD).setText("File name:");
				Text filenameHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(filenameHolder, SWT.Modify), getModel().filename);
				getModel().filename.setValue(biomarker.getName());

				ComboViewer separatorCombo = Utils.newComboViewer(c, "Separator character:", Arrays.asList(TAB, COMMA, SPACE, DOT));
				IObservableValue widgetObservable = ViewersObservables.observeSingleSelection(separatorCombo);
				dbc.bindValue(widgetObservable,  getModel().separator, new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("Separator")), null);
				getModel().separator.setValue(TAB);
				
				
//				new CLabel(c, SWT.BOLD).setText("Archivo:");
//				Text fileHolder = new Text(c, SWT.BORDER);

//				dbc.bindValue(SWTObservables.observeText(fileHolder, SWT.Modify), getModel().path,//
//						new UpdateValueStrategy().setAfterConvertValidator(new FilePathValidator()), null);

				GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(c);
				setControl(c);
				
				this.setTitle("Export the gene IDs of your gene signature");
				this.setDescription("Select the destination folder and file. You can also configure the separator char.");
			}

		};
	}

	@Override
	public final boolean performFinish() {
		boolean result = true;
		final String absoluteFilename = this.getModel().directory.getValue().toString() + OSDependentConstants.FILE_SEPARATOR + this.getModel().filename.getValue().toString();
		final String separator = convertSeparator(getModel().separator.getValue().toString());
		final Holder<Message> hm = new Holder<Message>();
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					//doFinish(biomarker, parameters);
					new ExportGenesToCSVFileCommand(biomarker, parameters, absoluteFilename, separator).execute();
					hm.hold(Message.info("The gene signature information was exported succesfully to: " + absoluteFilename));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			hm.hold(Message.error("Se encontraron fallos durante la exportación", e));
			result = false;
		}
		MessageManager.INSTANCE.add(hm.value()).openView();
		return result;
	}

	private Biomarker selectedBiomarker;

	private Biomarker findActiveBiomarker() {
		return selectedBiomarker;
	}

	/**
	 * ejecutar la acción de exportación del biomarcador b. <b>es proferible no
	 * debe modificar la vista desde este método</b>
	 * 
	 * @param b
	 * @param parameters
	 */
	protected void doFinish(Biomarker b, Map<String, String> parameters) {
		//new ExportGenesToCSVFileCommand(b, parameters).execute();
		//new ExportGenesToCSVFileCommand(b, absoluteFilename, (Boolean) this.getModel().includeClinicalData.getValue(), (Boolean) this.getModel().includeHeader.getValue(), (Boolean) this.getModel().includeExpressionData.getValue(), (Boolean) this.getModel().includeCluster.getValue(), convertSeparator(this.getModel().numericSeparator.getValue().toString()).charAt(0), convertSeparator(this.getModel().columnSeparator.getValue().toString()), true).execute();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}
	
	
	private String convertSeparator(String separator) {
		if (separator.equals(TAB)) return "\t";
		if (separator.equals(COMMA)) return ",";
		if (separator.equals(SPACE)) return " ";
		if (separator.equals(DOT)) return ".";
		return "\t";
	}
}
