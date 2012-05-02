package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Monitors;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.utils.monitor.Monitor;

/**
 * 
 * 
 * Mantiene el modelo del wizard, crea las páginas del wizard a partir de los
 * descriptores configurados
 * 
 * @author Diego Martínez
 * 
 * @param <T>
 * @see #createWizardmodel
 */
public abstract class AbstractWizard<T> extends Wizard implements IWorkbenchWizard {

	public AbstractWizard() {

	}

	private WizardModel model = createWizardModel();

	protected WizardModel wizardModel() {
		return model;
	}

	/**
	 * Inicializa el wizardmodel
	 * 
	 * @return
	 */
	protected WizardModel createWizardModel() {
		return new WizardModel();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		if (initialized)
			return;
		initialized = true;
		for (final WizardPageDescriptor pd : createPagesDescriptors()) {
			addPage(new WizardPage(pd.getPageName(), pd.getTitle(), pd.getImageDescriptor()) {

				@Override
				public void createControl(Composite parent) {
					final DataBindingContext dbc = new DataBindingContext();

					WizardPageSupport.create(this, dbc);
					Composite control = pd.create(this, parent, dbc, wizardModel());

					fillDefaultsIfNecesary(control);

					setControl(control);
				}

				private void fillDefaultsIfNecesary(Composite control) {
					if (control.getLayout() == null)
						GridLayoutFactory.fillDefaults().numColumns(1).generateLayout(control);
				}

				@Override
				public boolean isPageComplete() {
					return pd.isPageComplete(/* this? */wizardModel());
				}

			});
		}
	}

	private boolean blockOnOpen = true;

	public AbstractWizard<T> blockOnOpen(boolean block) {
		blockOnOpen = block;
		return this;
	}

	public AbstractWizard<T> blockOnOpen() {
		return blockOnOpen(true);
	}

	private boolean initialized = false;

	/**
	 * 
	 * @return devuelve el resultado del open del diálogo, true si se cerró ok y
	 *         false si se canceló la operaciòn
	 */
	public boolean open() {
		WizardDialog d = new WizardDialog(PlatformUIUtils.findShell(), this);

		this.init(PlatformUI.getWorkbench());
		d.setBlockOnOpen(blockOnOpen);

		// TODO resolver con scrollbars
		d.setPageSize(400, 450);
		return d.open() == Dialog.OK;
	}

	/**
	 * inicializa el wizard sin selección actual
	 * 
	 * @param workbench
	 */
	// TODO conseguir la selección actual
	public void init(IWorkbench workbench) {
		this.init(workbench, StructuredSelection.EMPTY);
	}

	@Override
	public boolean canFinish() {
		for (IWizardPage page : getPages())
			if (page.getErrorMessage() != null)
				return false;
		return super.canFinish();
	}

	protected abstract List<WizardPageDescriptor> createPagesDescriptors();

	private static ExecutorService exec = Executors.newFixedThreadPool(1);

	// TODO refactor
	@Override
	public boolean performFinish() {

		MessageManager.INSTANCE.clear();

		configureParameters();

		Job j = new Job(getTaskName()) {

			@Override
			protected IStatus run(final IProgressMonitor progressMonitor) {

				progressMonitor.beginTask(getTaskName(), IProgressMonitor.UNKNOWN);

				final Future<T> holder = exec.submit(new Callable<T>() {
					@Override
					public T call() throws Exception {
						Monitor m = Monitors.adapt(progressMonitor);
						return backgroundProcess(m);
					}

				});

				T o = null;
				try {
					o = holder.get(); // join
				} catch (Exception e) {
					e.printStackTrace();
					MessageManager.INSTANCE.add(Message.error("¡Error inesperado!", e));
				}

				final T oo = o;
				PlatformUIUtils.findDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						try {
							doInUI(oo);
							MessageManager.INSTANCE.add(Message.info("¡Operación realizada con éxito!"));
						} catch (Exception e) {
							e.printStackTrace();
							MessageManager.INSTANCE.add(Message.error("¡Error inesperado!", e));
						}
					}
				});

				progressMonitor.done();
				return Status.OK_STATUS;
			}

		};
		j.setUser(true);
		j.setPriority(Job.LONG);
		j.schedule();

		return true;

	}

	protected abstract String getTaskName();

	/**
	 * Permite configurar los parámetros dentro del Realm/ui-thread, el cual es
	 * necesario para poder acceder a los valores del model.
	 * 
	 */
	// TODO revisar si se puede resolver dentro del WizardModel el acceso con el
	// realm que va
	protected void configureParameters() {
	}

	/**
	 * Procesamiento fuera del ui-thread <b>no se puede modificar la vista
	 * directamente</b> tiene que estar contextualizada o explícitamente con
	 * Display.[a]syncExec() <br>
	 * <b>No</b> Se tiene acceso al wizardModel() directamente, para eso usar
	 * {@link #configureParameters()}
	 * 
	 * @see configureParameters()
	 * 
	 * @param monitor
	 *            puede ser una barra de progreso donde se va indicando el esta
	 *            del procesamiento
	 * @return
	 */
	protected abstract T backgroundProcess(Monitor monitor) throws Exception;

	/**
	 * Procesa dentro del ui-thread, permitiendo ejecutar código que interactúe
	 * con este, es definitiva, con la vista. por ejemplo abrir un editor.
	 * 
	 * @param result
	 *            es el resultado de {@link #backgroundProcess(Monitor)}
	 */
	protected abstract void doInUI(T result) throws Exception;

}
