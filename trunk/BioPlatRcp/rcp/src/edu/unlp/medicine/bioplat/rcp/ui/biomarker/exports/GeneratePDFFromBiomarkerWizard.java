package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.widgets.DirectoryText;
import edu.unlp.medicine.bioplat.rcp.widgets.TextWithSelectionButton;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.GeneratePDFFromBiomarkerCommand;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.r4j.constants.OSDependentConstants;
import edu.unlp.medicine.utils.monitor.Monitor;

public class GeneratePDFFromBiomarkerWizard extends AbstractWizard<Void> implements
		IExportWizard {

	private Biomarker biomarker;
	private Map<String, String> properties = new HashMap<String, String>();
	private String absoluteFilename = "";
	
	public GeneratePDFFromBiomarkerWizard(Biomarker b) {
		this.biomarker = b;
	}
	
	public void init(){
		this.setWindowTitle("Export BioPlat experiment to 'PDF file");
		addPage(createWizardPage());
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.setWindowTitle("Export BioPlat experiment to 'PDF file");
		addPage(createWizardPage());
	}


	private class WizardModel {
		IObservableValue filename = new WritableValue("biomaker", String.class);

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
				getModel().directory.setValue(System.getProperty("user.dir"));
				getModel().filename.setValue("");

				new CLabel(c, SWT.BOLD).setText("File name:");
				Text filenameHolder = new Text(c, SWT.BORDER);
				dbc.bindValue(SWTObservables.observeText(filenameHolder, SWT.Modify), getModel().filename);
				getModel().filename.setValue(biomarker.getName());

				GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(c);
				setControl(c);
				
				this.setTitle("Export configuration");
				this.setDescription("Select the destination folder and file. You can also parametrize the data to include.");

			}
		};
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTaskName() {
		return "Generate PDF From Biomarker";
	}

	@Override
	public boolean logInTheMessageView() {
		return false;
	}
	
	@Override
	protected Void backgroundProcess(Monitor monitor) throws Exception {
		properties.put("targetFile", absoluteFilename);

		new GeneratePDFFromBiomarkerCommand(biomarker, properties).execute();
		MessageManager.INSTANCE.add(Message.info("The Gene Signature " + biomarker.getName() + " and its statistics analysis, was succesfully exported to the file " + new File(absoluteFilename).getAbsoluteFile()));
		
		return null;
	}

	@Override
	protected void doInUI(Void result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		absoluteFilename = this.getModel().directory.getValue().toString() + OSDependentConstants.FILE_SEPARATOR + this.getModel().filename.getValue().toString() + ".pdf";
		return super.performFinish();
	}


	
	
}
