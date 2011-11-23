package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import edu.unlp.medicine.bioplat.rcp.ui.utils.Models;
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
		// TODO Auto-generated constructor stub
	}

	private String text;
	private Map<String, String> parameters = new HashMap<String, String>();

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		addPage(createWizardPage());
	}

	private WizardPage createWizardPage() {
		return new WizardPage("export to file") {
			{
				setPageComplete(false);
			}

			@Override
			public void createControl(Composite parent) {

				Composite c = new Composite(parent, SWT.BORDER);
				c.setLayout(GridLayoutFactory.fillDefaults().create());

				new CLabel(c, SWT.BOLD).setText("separador:");

				Text separatorHolder = new Text(c, SWT.BORDER);
				separatorHolder.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						parameters.put(ExportGenesToCSVFileCommand.SEPARATOR, ((Text) e.getSource()).getText());
					}
				});
				separatorHolder.setText(",");

				new CLabel(c, SWT.BOLD).setText("archivo:");
				Text fileHolder = new Text(c, SWT.BORDER);
				fileHolder.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						text = ((Text) e.getSource()).getText();
						File file = new File(text);
						setPageComplete(!file.isDirectory());
						parameters.put(ExportGenesToCSVFileCommand.PATH_OF_THE_OUTPUT_FILE, text);
					}
				});
				fileHolder.setText("c:/tmp/b");
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
