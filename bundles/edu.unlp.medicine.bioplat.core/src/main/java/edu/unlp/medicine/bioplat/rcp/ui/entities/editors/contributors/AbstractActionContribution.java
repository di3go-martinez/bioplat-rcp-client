package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import java.util.List;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.generic.AbstractEntity;

/**
 * 
 * @author diego martínez
 * 
 * @param <T>
 */
public abstract class AbstractActionContribution<T extends AbstractEntity> extends Action implements ActionContribution<T> {

	// active editor es necesatio para saber la selección actual
	// TODO revisar cuándo y por qué puede tirar error
	private IEditorPart activeEditor;

	public AbstractActionContribution() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addPostSelectionListener(new ISelectionListener() {
			
			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				activeEditor = part.getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
				
			}
		});
		
		/*addWindowListener(new IWindowListener() {

			@Override
			public void windowOpened(IWorkbenchWindow window) {
				// TODO revisar
				windowActivated(window);
			}

			@Override
			public void windowDeactivated(IWorkbenchWindow window) {

			}

			@Override
			public void windowClosed(IWorkbenchWindow window) {
				// PlatformUI.getWorkbench().removeWindowListener(this);
			}

			@Override
			public void windowActivated(IWorkbenchWindow window) {
				activeEditor = editor(window);
			}

			private IEditorPart editor(IWorkbenchWindow window) {
				return activeEditor = window.getActivePage().getActiveEditor();
			}
		});*/
	}

	protected <T extends ISelection> T getSelection() {
		final Holder<ISelection> sh = Holder.create();
		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				sh.hold(getCurrentEditor().getSite().getSelectionProvider().getSelection());
			}
		});

		T ms = (T) sh.value();
		return ms;
	}

	protected IEditorPart getCurrentEditor() {
		return activeEditor;
	}

	private ModelProvider<T> modelProvider;

	@Override
	public IAction action() {
		return this;
	}

	@Override
	public void modelProvider(ModelProvider<T> modelProvider) {
		this.modelProvider = modelProvider;
	}

	@Override
	public void image(ImageDescriptor image) {
		action().setImageDescriptor(image);
	}

	protected T model() {
		return modelProvider.model();
	}

	@Override
	public void caption(String caption) {
		setText(caption);
	}

	/**
	 * Corre por fuera del thread ui, así que para levantar un diálogo por
	 * ejemplo se deberá pedir explicitamente el thread correspondiente; como
	 * consecuencia de lo anterior, este método corre por fuera del Realm (para
	 * estar dentro hay que hacerlo explícitamente)
	 */
	@Override
	public abstract void run();

	private boolean onMenu, onToolbar;
	private String groupId;

	@Override
	public void group(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * 
	 * @return the group al que pertencece la acción o null si no pertenece a
	 *         algún grupo en particular
	 */
	public String group() {
		return groupId;
	}

	@Override
	public void onMenu(Boolean b) {
		this.onMenu = b;
	}

	@Override
	public boolean onMenu() {
		return onMenu;
	}

	@Override
	public boolean onToolbar() {
		return this.onToolbar;
	}

	@Override
	public void onToolbar(Boolean b) {
		this.onToolbar = b;
	}

	/**
	 * It runs the <code>runnable</code> in the thread swt ui and in a safe way
	 * 
	 * @param runnable
	 */
	protected void inDisplay(final Runnable runnable) {
		SafeRunner.run(new ISafeRunnable() {

			@Override
			public void run() throws Exception {
				PlatformUIUtils.findDisplay().syncExec(runnable);
			}

			@Override
			public void handleException(Throwable exception) {
				logger.error("Error executing the action " + this, exception);
			}
		});

	}

	private String getGeneListAsString(List<String> genesAddedList) {
		StringBuffer result = new StringBuffer("");
		for (String id : genesAddedList) {
			result.append(", " + id);
		}
		return result.toString().substring(2, result.toString().length());
		
		
	}
	
	private Logger logger = LoggerFactory.getLogger(AbstractActionContribution.class);

}
