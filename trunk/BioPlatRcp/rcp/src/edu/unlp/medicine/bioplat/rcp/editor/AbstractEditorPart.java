package edu.unlp.medicine.bioplat.rcp.editor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.progress.IProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.input.AbstractEditorInput;
import edu.unlp.medicine.bioplat.rcp.utils.Service;
import edu.unlp.medicine.entity.generic.AbstractEntity;

/**
 * Implementación abstracta para los editores
 * 
 * @author Diego Martínez
 * @version $Revision:$
 * @updatedBy $Author:$ on $Date:$
 */
public abstract class AbstractEditorPart<T extends AbstractEntity> extends EditorPart implements ISaveablePart2, ModelProvider<T> {

	private static Logger logger = LoggerFactory.getLogger(AbstractEditorPart.class);

	private class ForDirtyObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			firePropertyChange(EditorPart.PROP_DIRTY);
		}
	}

	public AbstractEditorPart() {
		this(true);
	}

	public AbstractEditorPart(boolean updatableTitle) {
		addPartPropertyListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				System.out.println(event);
			}
		});
		this.updatableTitle = updatableTitle;
	}

	private Control focusReceptor;

	// por ejemplo cuando se usa como contendor de un multipleEditor @see
	// MultiPageBiomarkerEditor
	private boolean updatableTitle = true;

	@Override
	public final void doSave(IProgressMonitor monitor) {
		try { // TODO reveer
			getProgressService().run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					// TODO Auto-generated method stub
					// Save here
					monitor.beginTask("Guardando", IProgressMonitor.UNKNOWN);
					doSave0();
					getEditorInput().model().clear();
					monitor.done();
				}

			});

			firePropertyChange(EditorPart.PROP_DIRTY);

			if (updatableTitle)
				setPartName(model().id());
			// firePropertyChange(EditorPart.PROP_TITLE);

		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void doSave0() {
		delay(500);
		// RepositoryFactory.getRepository().save(model());
	}

	private IProgressService getProgressService() {
		return Service.INSTANCE.getProgressService();
	}

	private void delay(final int mills) {
		BusyIndicator.showWhile(null, new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(mills);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void doSaveAs() {
		if (isSaveAsAllowed())
			System.err.println("¡'Save as' no está implementado todavía!");
	}

	@Override
	public final void init(IEditorSite site, IEditorInput input) throws PartInitException {

		// Es obligatorio guardarse tanto el input como el site
		setInput(input);
		setSite(site);
		model().addObserver(new ForDirtyObserver());
		setPartName(model().id());
		// firePropertyChange(PROP_TITLE);
		ISelectionProvider sp = createSelectionProvider();

		getSite().setSelectionProvider(sp);
	}

	protected ISelectionProvider createSelectionProvider() {
		return new ISelectionProvider() {

			@Override
			public void setSelection(ISelection selection) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeSelectionChangedListener(ISelectionChangedListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public ISelection getSelection() {
				MultipleSelection cs = new MultipleSelection();
				cs.put(Constants.MODEL, new StructuredSelection(model())).add(getAdditionalSelections());
				return cs;
			}

			@Override
			public void addSelectionChangedListener(ISelectionChangedListener listener) {
				// TODO Auto-generated method stub

			}
		};
	}

	protected Map<Object, IStructuredSelection> getAdditionalSelections() {
		return Collections.emptyMap();
	}

	/**
	 * Está hecho en principio porque no se refrescan las listas en grillas por
	 * ejemplo... sacar cuando funcione correctamente el databinding de
	 * colecciones en el sentido modelo->vista
	 * 
	 * @return
	 * @deprecated
	 */
	@Deprecated
	protected Observer createModificationObserver() {
		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				// do nothing
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public AbstractEditorInput<T> getEditorInput() {
		return (AbstractEditorInput<T>) super.getEditorInput();
	}

	@Override
	public final boolean isDirty() {
		return model().isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public final void createPartControl(Composite parent) {
		try {
			doCreatePartControl((Composite) (focusReceptor = parent));
			model().addObserver(createModificationObserver());
		} catch (Exception e) {
			logger.error("Error creando el editor " + this, e);
		}
	}

	protected abstract void doCreatePartControl(Composite parent);

	@Override
	public void setFocus() {
		if (focusReceptor != null)
			focusReceptor.setFocus();
	}

	@Override
	public final T model() {
		return getEditorInput().model();
	}

	protected void focusReceptor(Control c) {
		this.focusReceptor = c;
	}

	@Override
	public int promptToSaveOnClose() {
		// TODO tener mayor control
		return ISaveablePart2.DEFAULT;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) // quicktest
			return true;

		if (!(obj instanceof AbstractEditorInput)) // or obj is null
			return false;

		try {
			// TODO revisar mejor... problema con los editores que están
			// contenidos en otros editores... ya está resuelto esto, no?
			AbstractEditorPart<?> another = (AbstractEditorPart<?>) obj;
			return getClass().equals(another.getClass()) && this.getEditorInput().equals(another.getEditorInput());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// FIXME no está bien implementado
	@Override
	public int hashCode() {
		return getEditorInput().hashCode();
	}
}
