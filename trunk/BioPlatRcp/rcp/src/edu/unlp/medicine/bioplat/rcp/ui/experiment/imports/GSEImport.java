package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.NotImplementedException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Monitors;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.utils.monitor.Monitor;

public class GSEImport extends Wizard implements IImportWizard {

	public GSEImport() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		addPage(createFirstPage());
	}

	private WizardModel model = createWizardModel();

	private WizardModel model() {
		return model;
	}

	private WizardModel createWizardModel() {
		return new WizardModel().add("URL", new WritableValue("http://", URL.class));
	}

	private WizardPage createFirstPage() {
		return new WizardPage("GSE", "Indicar la url del experimento a importar", null) {

			@Override
			public void createControl(Composite parent) {

				DataBindingContext dbc = new DataBindingContext();
				WizardPageSupport.create(this, dbc);

				Composite c = new Composite(parent, SWT.BORDER);

				new CLabel(c, SWT.BOLD).setText("URL:");

				Text urlHolder = new Text(c, SWT.BORDER);

				dbc.bindValue(SWTObservables.observeText(urlHolder, SWT.Modify), model().valueHolder("URL"));

				GridLayoutFactory.swtDefaults().numColumns(1).generateLayout(c);
				setControl(c);

			}
		};
	}

	private static ExecutorService exec = Executors.newFixedThreadPool(1);

	@Override
	public boolean performFinish() {

		MessageManager.INSTANCE.clear();

		configureParamenters();

		Job j = new Job("Importando experimento") {

			@Override
			protected IStatus run(final IProgressMonitor progressMonitor) {

				Future<Experiment> holder = exec.submit(new Callable<Experiment>() {
					@Override
					public Experiment call() throws Exception {
						Monitor m = Monitors.adapt(progressMonitor);
						return execute(m);
					}

				});

				try {
					final Experiment e = holder.get(); // join
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							PlatformUIUtils.openEditor(e, ExperimentEditor.id());
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					MessageManager.INSTANCE.add(Message.error("Falló el importador", e));
					return Status.CANCEL_STATUS;
				}

				MessageManager.INSTANCE.add(Message.info("Importación finalizada con éxito"));
				return Status.OK_STATUS;
			}

		};
		j.setUser(true);
		j.setPriority(Job.LONG);
		j.schedule();

		return true;

	}

	/**
	 * Permite configurar los parámetros dentro del Realm, el cual es necesario
	 * para poder acceder a los valores del model...
	 * 
	 */
	// TODO revisar si se puede resolver dentro del WizardModel el acceso con el
	// realm que va
	private void configureParamenters() {
		try {
			url = new URL(model().value("URL").toString());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	private URL url;

	private Experiment execute(Monitor m) {
		throw new NotImplementedException("Falta obtener el experimento desde " + url);

	}

}
