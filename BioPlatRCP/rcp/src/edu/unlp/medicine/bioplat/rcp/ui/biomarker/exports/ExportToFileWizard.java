package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import edu.unlp.medicine.bioplat.rcp.ui.utils.Models;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.FilePathValidator;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.ExportGenesToCSVFileCommand;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * exportador que tiene como parámetros el separador y el nombre completo del
 * archivo a escribir
 * 
 * @author diego
 * 
 */
public class ExportToFileWizard extends Wizard implements IExportWizard {

	public ExportToFileWizard() {
	}

	private Map<String, String> parameters = new HashMap<String, String>();

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		addPage(createWizardPage());
	}

	private class WizardModel {
		IObservableValue separator = new WritableValue(".", String.class);

		IObservableValue path = new WritableValue(null, String.class);
		{
			separator.addChangeListener(new IChangeListener() {

				@Override
				public void handleChange(ChangeEvent event) {
					parameters.put(ExportGenesToCSVFileCommand.SEPARATOR, separator.getValue().toString());
				}
			});

			path.addChangeListener(new IChangeListener() {

				@Override
				public void handleChange(ChangeEvent event) {
					parameters.put(ExportGenesToCSVFileCommand.PATH_OF_THE_OUTPUT_FILE, path.getValue().toString());
				}
			});
			// le seteo valores por default
			separator.setValue(",");
			path.setValue("c:/tmp/biomarker");
		}
	}

	private WizardModel model = new WizardModel();

	private WizardModel getModel() {
		return model;
	}

	private WizardPage createWizardPage() {
		return new WizardPage("export to file") {
			{
				setPageComplete(false);
			}

			@Override
			public void createControl(Composite parent) {

				DataBindingContext dbc = new DataBindingContext();
				WizardPageSupport.create(this, dbc);

				Composite c = new Composite(parent, SWT.BORDER);
				// c.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());

				new CLabel(c, SWT.BOLD).setText("Separador:");

				Text separatorHolder = new Text(c, SWT.BORDER);

				dbc.bindValue(SWTObservables.observeText(separatorHolder, SWT.Modify), getModel().separator);

				new CLabel(c, SWT.BOLD).setText("Archivo:");
				Text fileHolder = new Text(c, SWT.BORDER);

				dbc.bindValue(SWTObservables.observeText(fileHolder, SWT.Modify), getModel().path,//
						new UpdateValueStrategy().setAfterConvertValidator(new FilePathValidator()), null);

				GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(c);
				setControl(c);
			}

		};
	}

	@Override
	public boolean performFinish() {
		Biomarker b = Models.getInstance().getActiveBiomarker();
		doFinish(b, parameters);
		return true;
	}

	protected void doFinish(Biomarker b, Map<String, String> parameters) {
		new ExportGenesToCSVFileCommand(b, parameters).execute();
	}
}
