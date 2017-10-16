package edu.unlp.medicine.bioplat.rcp.utils.jobs;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.ui.progress.IProgressConstants;
import org.slf4j.Logger;

/**
 * Extensión de Jobs de Eclipse<br/>
 * fecha 01/03/2011
 * 
 * @author Diego Martínez <a>diemar@mecon.gov.ar</a>
 */
public abstract class EJob extends Job {

	private IStatus status = null;

	public EJob(String name) {
		super(name);
		initialize();
	}

	private void initialize() {
		setUser(true);
		setPriority(Job.LONG);
	}

	/**
	 * Método principal del job, corre en el thread que se abre para la ejecución
	 * asincrónica
	 * 
	 * @param monitor
	 * @return
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected final IStatus run(IProgressMonitor monitor) {

		try {

			doWork(monitor);

			status = (monitor.isCanceled()) ? Status.CANCEL_STATUS : Status.OK_STATUS;

			setProperty(IProgressConstants.KEEP_PROPERTY, !isModal());
			if (isModal())
				showModalResult();
			else
				setProperty(IProgressConstants.ACTION_PROPERTY, getCompletedAction());

			return status;

		} catch (Exception e) {
			return ValidationStatus.error("Unexpected Error", e);
			// throw new RuntimeException(e);
		}

	}

	/**
	 * realiza el trabajo del job <b>IMPORTARTE: la ejecución de este método es
	 * fuera del thread ui de swt, es decir, no se puede acceder a modificar la
	 * vista directamente, en caso de ser necesario, ver: {@link ThreadUIUtils} </b>
	 * 
	 * @param monitor
	 */
	protected abstract void doWork(IProgressMonitor monitor);

	/**
	 * Una vez que termina el procesamiento de doWork, se ejecuta un
	 * postprocesamiento de la siguiente manera:
	 * <li>Si el usuario mandó el job al background se ejecutará una vez que el
	 * usuario lo solicite
	 * <li>se ejecuta inmediatamnete si el usuario no mando el job al back
	 */
	protected void doOnCallBack() {
	}

	/**
	 * Determina si la barra de progreso está modal o no, lo cual significa que el
	 * job se mando a segundo plano
	 * 
	 * @return true si el job no se mando a segundo plano, false en otro caso
	 */
	private boolean isModal() {
		Boolean isModal = (Boolean) getProperty(IProgressConstants.PROPERTY_IN_DIALOG);
		return (isModal == null) ? false : isModal;
	}

	/**
	 * Si el procesamiento <code>doWork</code> terminó e isModal == true, se ejecuta
	 * el siguiente método
	 */
	private void showModalResult() {
		getCompletedAction().run();
	}

	/**
	 * Devuelve una action que ejecuta el callback
	 */
	private IAction getCompletedAction() {
		return new Action("OK") {

			@Override
			public void run() {
				try {
					if (Status.OK_STATUS.equals(status))
						doOnCallBack();
				} catch (Exception e) {
					throw new RuntimeException(e.getCause());
				}
			}
		};
	}

	public static void runjob(final String name, final SafeRunnable runnable) {
		new EJob(name) {

			@Override
			protected void doWork(IProgressMonitor monitor) {
				monitor.beginTask(name, IProgressMonitor.UNKNOWN);
				try {
					runnable.run();
				} catch (Exception e) {
					logger.error("Job execution failed", e);
				} finally {
					monitor.done();
				}

			}
		}.schedule();;
	}

	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EJob.class);
}
