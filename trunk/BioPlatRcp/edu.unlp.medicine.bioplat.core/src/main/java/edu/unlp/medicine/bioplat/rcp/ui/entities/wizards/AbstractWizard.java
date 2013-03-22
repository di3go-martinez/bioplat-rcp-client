package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressConstants;

import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.Monitors;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.exceptions.BioplatException;
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
		setNeedsProgressMonitor(true);
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

	private Map<IWizardPage, WizardPageDescriptor> map = Maps.newHashMap();

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		if (initialized)
			return;
		initialized = true;
		IWizardPage page;
		for (final WizardPageDescriptor pd : createPagesDescriptors()) {
			addPage(page = new WizardPage(pd.getPageName(), pd.getTitle(), pd.getImageDescriptor()) {

				@Override
				public void createControl(Composite parent) {

					final DataBindingContext dbc = new DataBindingContext();

					WizardPageSupport.create(this, dbc);
					Composite control = pd.create(this, parent, dbc, wizardModel());

					fillDefaultsIfNecesary(control);

					setControl(control);

					// Point size = getShell().computeSize(510, 540);
					//getShell().setSize(510, 540);

				}

				private void fillDefaultsIfNecesary(Composite control) {
					if (control.getLayout() == null)
						GridLayoutFactory.fillDefaults().numColumns(1).generateLayout(control);
				}

				@Override
				public boolean isPageComplete() {
					return pd.isPageComplete(/* this? */wizardModel());
				}

				@Override
				public boolean canFlipToNextPage() {
					return isPageComplete() && (pd.hasResultPage() || getNextPage() != null);
				}

//				@Override
//				public Shell getShell() {
//					return super.getShell();
//				}

			});

			map.put(page, pd);

			if (pd.hasResultPage()) {
				// TODO pd.createResultPage();
				IWizardPage p = new ResultPage(this, pd);
				map.put(p, null);
				addPage(p);
			}

		}
	}

	private boolean initialPage = true;

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		// Caso especial...
		if (initialPage) {
			initialPage = false;
			return super.getNextPage(page);
		}

		IWizardPage next = super.getNextPage(page);
		if (next instanceof ResultPage) {
			ResultPage p = (ResultPage) next;
			p.lazyCreateControl();
		}
		return next;
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		// TODO Auto-generated method stub
		return super.getPreviousPage(page);
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

	public int getMinimumWith() {
		return 400;
	}

	/**
	 * 
	 * @return devuelve el resultado del open del diálogo, true si se cerró ok y
	 *         false si se canceló la operaciòn
	 */
	public boolean open() {
		WizardDialog d = new WizardDialog(PlatformUIUtils.findShell(), this)
//		{
//			@Override
//			protected Control createContents(Composite parent) {
//				Control result = super.createContents(parent);
//				getShell().addListener(SWT.Resize, new Listener() {
//					@Override
//					public void handleEvent(Event event) {
//						getShell().setSize(100, 100);
//					}
//				});
//				return result;
//			}
//		}
		;

		this.init(PlatformUI.getWorkbench());
		d.setBlockOnOpen(blockOnOpen);

		// TODO resolver con scrollbars
		d.setPageSize(400, 450);
		// d.setMinimumPageSize(this.getMinimumWith(), 450);
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

	/**
	 * Crea los descriptores de páginas del wizard.
	 * 
	 * @return
	 */
	protected abstract List<WizardPageDescriptor> createPagesDescriptors();

	// TODO probar el pool, antes tenía 1 por problemas de concurrencia ¿?...
	private static ExecutorService exec = Executors.newFixedThreadPool(10);

	// TODO refactor
	@Override
	public boolean performFinish() {
		// Ahora no se borran los mensajes de la vista
		// MessageManager.INSTANCE.clear();

		configureParameters();

		// TODO migrar a EJob
		Job j = new Job(getTaskName()) {

			@Override
			protected IStatus run(final IProgressMonitor progressMonitor) {

				// TODO sacar cuando se migre a EJob
				setProperty(IProgressConstants.KEEP_PROPERTY, true);

				progressMonitor.beginTask(getTaskName(), IProgressMonitor.UNKNOWN);

				final Future<T> holder = exec.submit(new Callable<T>() {
					@Override
					public T call() throws Exception {
						Monitor m = Monitors.adapt(progressMonitor);
						return backgroundProcess(m);
					}

				});

				// TODO revisar mejor lo del error holder y lo de status...
				final Holder<Throwable> errorHolder = Holder.create(null);
				//IStatus status = ValidationStatus.ok();
				T o = null;
				
				try {
					o = holder.get(); // join
				}
				catch (Exception e) {
					Throwable t = (e.getCause() != null) ? e.getCause() : e;
					errorHolder.hold(t);
				}

				final T oo = o;
				PlatformUIUtils.findDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						try {
							if (errorHolder.value()!=null){
								progressMonitor.done();
								doInUIError(errorHolder.value());
								addMessageErrorToMessageView(errorHolder.value());
								
							}
							else{
								progressMonitor.done();
								doInUI(oo);
								addMessageToMessageView(oo);
							}
						} catch (Exception e) {
							e.printStackTrace();
							MessageManager.INSTANCE.add(Message.error(defaultErrorMsg(e), e));
						}
					}

					
				});
				if (errorHolder.value()==null) return ValidationStatus.ok();
				else return ValidationStatus.error(errorHolder.value().getMessage(), errorHolder.value());
			}


//			private IStatus processBioplatException(BioplatException bioplatE) {
//				IStatus status = bioplatE.isWarning() ? ValidationStatus.warning(getErrorMessage().getText()):ValidationStatus.error(getErrorMessage().getText(), bioplatE);
//				return status;
//				
//			}
//
//			private void setErrorOnMessageView(Throwable t){
//				//Si no setearon error desde afuera seteo uno por defecto
//				if (getErrorMessage()==null) setErrorMessage(Message.error(defaultErrorMsg(t), t));
//				MessageManager.INSTANCE.add(getErrorMessage());
//			}
//
//			
			private String defaultErrorMsg(Throwable t) {
				return "Unexpected error executing " + getTaskName() + ": " + t.getMessage();
			}

		};
		j.setUser(true);
		j.setPriority(Job.LONG);
		j.schedule();

		return true;

	}
	
	
	
	Message errorMessage=null;
	


	public Message getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Message errorMessage) {
		this.errorMessage = errorMessage;
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
	
	public void doInUIError(Throwable e) throws Exception{
		
	}

	public void addMessageToMessageView(T oo) {
		MessageManager.INSTANCE.add(Message.info(getTaskName() + " was succesfully executed"));
		
	}
	
	private void addMessageErrorToMessageView(Throwable t) {
		MessageManager.INSTANCE.add(Message.error(t.getMessage(), t));
		
	}



}

class ResultPage extends WizardPage {

	private Composite parent;
	private boolean initialized = false;
	private WizardPageDescriptor pd; // PageDescriptor
	private AbstractWizard<?> w;

	public ResultPage(AbstractWizard<?> w, WizardPageDescriptor pd) {
		super("dummy page");
		this.pd = pd;
		this.w = w;
	}

	@Override
	public void createControl(Composite rparent) {
		this.parent = Widgets.createDefaultContainer(rparent);
		setControl(this.parent);
	}

	void lazyCreateControl() {
		if (!initialized) {
			pd.initializeResultPage(parent, w.wizardModel(), w, this);
			parent.layout(true);
			initialized = true;
		} else {
			pd.refreshResultPage(w.wizardModel(), w);
		}

	}

	@Override
	public boolean isPageComplete() {
		return pd.isResultPageComplete(w.wizardModel());
	}
}