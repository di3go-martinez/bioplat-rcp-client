package edu.unlp.medicine.bioplat.rcp.editor;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import edu.unlp.medicine.bioplat.rcp.editor.input.AbstractEditorInput;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.generic.AbstractEntity;

/**
 * Implementaci�n abstracta para los editores
 * 
 * @author Diego Mart�nez
 * @version $Revision:$
 * @updatedBy $Author:$ on $Date:$
 */
public abstract class AbstractEditorPart<T extends AbstractEntity> extends EditorPart implements ISaveablePart2 {

	private class ForDirtyObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			firePropertyChange(EditorPart.PROP_DIRTY);
		}
	}

	public AbstractEditorPart() {
		addPartPropertyListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				System.out.println(event);
			}
		});
	}

	private Control focusReceptor;

	@Override
	public final void doSave(IProgressMonitor monitor) {
        try { // TODO reveer
            PlatformUIUtils.activePage().getWorkbenchWindow().getWorkbench().getProgressService().run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    // TODO Auto-generated method stub
                    // Save here
                    monitor.beginTask("Guardando", IProgressMonitor.UNKNOWN);
                    delay(1000);
                    getEditorInput().model().clear();
                    monitor.done();
                }
            });

            firePropertyChange(EditorPart.PROP_DIRTY);
            setPartName(model().toString());
            // firePropertyChange(EditorPart.PROP_TITLE);
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
		// TODO Auto-generated method stub
		if (isSaveAsAllowed())
 System.err.println("�'Save as' no est� implementado todav�a!");
	}

	@Override
	public final void init(IEditorSite site, IEditorInput input) throws PartInitException {

		// Es obligatorio guardarse tanto el input como el site
		setInput(input);
		setSite(site);
		model().addObserver(new ForDirtyObserver());

		setPartName(model().toString());
		// firePropertyChange(PROP_TITLE);
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
		doCreatePartControl((Composite)(focusReceptor = parent));
	}

	protected abstract void doCreatePartControl(Composite parent);

	@Override
	public void setFocus() {
		if (focusReceptor != null)
			focusReceptor.setFocus();
	}

	protected T model() {
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

}
