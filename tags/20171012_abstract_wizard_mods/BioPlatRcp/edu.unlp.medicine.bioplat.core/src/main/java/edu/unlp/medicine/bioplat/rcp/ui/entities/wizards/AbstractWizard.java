package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.SystemUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.Monitors;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
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
	private static Logger logger = LoggerFactory.getLogger(AbstractWizard.class);

	public AbstractWizard() {
		setNeedsProgressMonitor(true);
	}

	private WizardModel model = createWizardModel();

	protected WizardModel wizardModel() {
		return model;
	}

	protected final <X> X value(String key) {
		return wizardModel().value(key);
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

	public int getWizardWidth() {
		return 300;
	}

	public int getWizardHeight() {
		return 600;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		if (initialized)
			return;
		initialized = true;
		IWizardPage page;
		for (final WizardPageDescriptor pageDescriptor : createPagesDescriptors()) {
			addPage(page = new WizardPage(pageDescriptor.getPageName(), pageDescriptor.getTitle(),
					pageDescriptor.getImageDescriptor()) {

				@Override
				public void createControl(Composite parent) {

					final DataBindingContext dbc = new DataBindingContext();

					WizardPageSupport.create(this, dbc);
					Composite control = pageDescriptor.create(this, parent, dbc, wizardModel());
					logger.trace("control created");

					fillDefaultsIfNecesary(control);

					setControl(control);

					fixSize();

				}

				private void fixSize() {
					if (!maximized()) {
						Point size = getShell().computeSize(getWizardWidth(), getWizardHeight());
						getShell().setSize(size);
						logger.trace("Shell size " + size);
					}
				}

				private void fillDefaultsIfNecesary(Composite control) {
					if (control.getLayout() == null) {
						GridLayoutFactory.fillDefaults().numColumns(1).generateLayout(control);
					}
				}

				@Override
				public boolean isPageComplete() {
					return pageDescriptor.isPageComplete(wizardModel());
				}

				@Override
				public boolean canFlipToNextPage() {
					if (pageDescriptor.isManualFlip()) {
						return pageDescriptor.isAllowFlip();
					}
					return isPageComplete() && (pageDescriptor.hasResultPage() || getNextPage() != null)
							&& pageDescriptor.allowContinueWizardSetup();
				}

				@Override
				public void setVisible(boolean visible) {
					super.setVisible(visible);
					if (visible) {
						pageDescriptor.doOnEnter();
					} else {
						pageDescriptor.doOnExit();
					}
				}

			});

			map.put(page, pageDescriptor);
			page.setDescription(pageDescriptor.getDescription());
			if (pageDescriptor.hasResultPage()) {
				// TODO pd.createResultPage();
				IWizardPage p = new ResultPage(this, pageDescriptor);
				map.put(p, null);
				addPage(p);
			}

		}

	}

	private boolean initialPage = true;

	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		// Caso especial... //TODO por qué, cómo y cuándo...
		if (initialPage) {
			logger.trace("get the next page by 'initialPage'; delegando a super");
			initialPage = false;
			return super.getNextPage(page);
		}

		IWizardPage next = super.getNextPage(page);
		if (next instanceof ResultPage) {
			logger.trace("Next is resulting page, lazy creation");
			ResultPage p = (ResultPage) next;
			p.lazyCreateControl();
		}
		if (next != null)
			logger.trace("returning the next wizard page for " + next.getDescription());
		else
			logger.trace("No more pages");

		// TODO determinar para qué está este código!
		Shell activeShell = PlatformUIUtils.findDisplay().getActiveShell();
		if (activeShell != null)
			activeShell.layout(true, true);

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
		// TODO llevar a:
		// init(PlatformUI.getWorkbench())
		// createWizardDialog().open()

		WizardDialog d = new WizardDialog(PlatformUIUtils.findShell(), this) {
			private boolean initialize = true;

			@Override
			protected Control createContents(Composite parent) {
				if (initialize) {
					getShell().setSize(Math.max(500, getShell().getSize().x), Math.max(500, getShell().getSize().y));
					PlatformUIUtils.center(getShell());
					initialize = false;
				}
				Control result = super.createContents(parent);
				// getShell().addListener(SWT.Resize, new Listener() {
				// @Override
				// public void handleEvent(Event event) {
				// getShell().setSize(500, 500);
				// }
				// });
				return result;
			}
		}

		;

		this.init(PlatformUI.getWorkbench());
		d.setBlockOnOpen(blockOnOpen);
		d.create();
		// FIXME ver
		// org.eclipse.ui.internal.handlers.WizardHandler.New.executeHandler(ExecutionEvent
		// event)
		d.getShell().setSize(Math.max(500, d.getShell().getSize().x), Math.max(500, d.getShell().getSize().y));
		PlatformUIUtils.center(d.getShell());
		// TODO resolver con scrollbars
		// d.setPageSize(400, 450);
		// d.setMinimumPageSize(this.getMinimumWith(), 450);

		if (maximized())
			maximize(d);

		return d.open() == Dialog.OK;

	}

	private void maximize(WizardDialog d) {
		Shell shell = d.getShell();
		shell.pack();
		shell.setMaximized(true);
	}

	protected boolean maximized() {
		return false;
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
		WizardPageDescriptor wpd = map.get(getContainer().getCurrentPage());

		if (wpd == null) {
			return true;
		}

		if (wpd.isManualFlip()) {
			return wpd.isAllowFinish();
		}

		for (IWizardPage page : getPages())
			if (page.getErrorMessage() != null)
				return false;

		// TODO acomodar por si la páginas de resultados deciden si parar el
		// curso del wizard
		boolean resultPage = wpd == null;
		return (!resultPage && !wpd.allowContinueWizardSetup()) || super.canFinish();
	}

	@Override
	public boolean performCancel() {
		if (map.get(getContainer().getCurrentPage()) != null) {
			WizardPageDescriptor wpd = map.get(getContainer().getCurrentPage());
			return wpd.performCancel(model);
		} else {
			return true;
		}

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
				// IStatus status = ValidationStatus.ok();
				T o = null;

				try {
					o = holder.get(); // threads' join
				} catch (Exception e) {
					Throwable t = (e.getCause() != null) ? e.getCause() : e;
					errorHolder.hold(t);
				}

				final T oo = o;
				PlatformUIUtils.findDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						try {
							progressMonitor.done();
							if (errorHolder.value() != null) {
								doInUIError(errorHolder.value());
								addErrorMessageToMessageView(errorHolder.value());
							} else {
								doInUI(oo);
								// IWorkbenchPage page = ;
								// page.setPartState(page.findViewReference("org.eclipse.ui.internal.introview"),
								// IWorkbenchPage.STATE_MINIMIZED);
								PlatformUIUtils.activePage()
										.hideView(PlatformUIUtils.findView("org.eclipse.ui.internal.introview"));
								addMessageToMessageView(oo);
							}
						} catch (Exception e) {
							e.printStackTrace();
							MessageManager.INSTANCE.add(Message.error(defaultErrorMsg(e), e));
						}
					}

				});
				if (errorHolder.value() == null)
					return ValidationStatus.ok();
				else
					return ValidationStatus.error(errorHolder.value().getMessage(), errorHolder.value());
			}

			// private IStatus processBioplatException(BioplatException
			// bioplatE) {
			// IStatus status = bioplatE.isWarning() ?
			// ValidationStatus.warning(getErrorMessage().getText()):ValidationStatus.error(getErrorMessage().getText(),
			// bioplatE);
			// return status;
			//
			// }
			//
			// private void setErrorOnMessageView(Throwable t){
			// //Si no setearon error desde afuera seteo uno por defecto
			// if (getErrorMessage()==null)
			// setErrorMessage(Message.error(defaultErrorMsg(t), t));
			// MessageManager.INSTANCE.add(getErrorMessage());
			// }
			//
			//
			private String defaultErrorMsg(Throwable t) {
				String msg = "Error executing the operation:  " + getTaskName();
				if (t.getMessage() != null && !t.getMessage().equals(""))
					msg = msg + ". Message:  " + t.getMessage();
				return msg;
			}

		};
		j.setUser(true);
		j.setPriority(Job.LONG);
		if (hasRule())
			j.setRule(getJobRule());
		j.schedule();

		return true;

	}

	private boolean hasRule() {
		return getJobRule() != null;
	}

	// no hay rule por defecto
	protected ISchedulingRule getJobRule() {
		return null;
	}

	Message errorMessage = null;

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
	 * @deprecated no va a ser más necesario cuando se haga el cambio de acceso en
	 *             la clase#método WizardModel#value (solo resta habilitarlo,
	 *             planificado para "largo plazo")
	 */
	// TODO revisar si se puede resolver dentro del WizardModel el acceso con el
	// realm que va
	@Deprecated
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
	 *            puede ser una barra de progreso donde se va indicando el esta del
	 *            procesamiento
	 * @return
	 */
	protected abstract T backgroundProcess(Monitor monitor) throws Exception;

	/**
	 * Procesa dentro del ui-thread, permitiendo ejecutar código que interactúe con
	 * este, es definitiva, con la vista. por ejemplo abrir un editor.
	 * 
	 * @param result
	 *            es el resultado de {@link #backgroundProcess(Monitor)}
	 */
	protected abstract void doInUI(T result) throws Exception;

	// TODO javadoc
	public void doInUIError(Throwable e) throws Exception {

	}

	public void addMessageToMessageView(T oo) {
		if (this.logInTheMessageView())
			MessageManager.INSTANCE.add(Message.info(getTaskName() + " was succesfully executed."));

	}

	public boolean logInTheMessageView() {
		return true;
	}

	private void addErrorMessageToMessageView(Throwable t) {
		logger.error("Ocurrió un error", t);
		MessageManager.INSTANCE.add(Message.error(t.getMessage()));

	}

}

class ResultPage extends WizardPage {

	private Composite parent;
	private boolean initialized = false;
	private WizardPageDescriptor pd; // PageDescriptor
	private AbstractWizard<?> w;

	public ResultPage(AbstractWizard<?> w, WizardPageDescriptor pd) {
		super("page for results");
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