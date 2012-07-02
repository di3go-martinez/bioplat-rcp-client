package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.application.Activator;
import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.actions.MessageViewOpenAction;
import edu.unlp.medicine.bioplat.rcp.ui.utils.extension.points.DefaultExtensionLoaderRunnable;
import edu.unlp.medicine.bioplat.rcp.ui.utils.extension.points.ExtensionPointLoader;

//TODO Ver si se puede usar para el selectionType por ejemplo
//TODO agregar configuración en populateMenu, por ejemplo, para agregar una operación al toolbar, al menu (por default)
//TODO agregar método para permitir la ejecución en el threadui... ?¿
//TODO agregado de otras opciones para las acciones, como por ejemplo imágenes
public abstract class AbstractEditorActionBarContributor<T> extends EditorActionBarContributor implements ModelProvider<T> {

	private List<ActionContribution<T>> actions = Lists.newArrayList();

	public AbstractEditorActionBarContributor() {
		populateByExtensionPoint(actions);
	}

	/**
	 * Se agregan las operaciones a realizar sobre una entidad al menu
	 * 
	 * @deprecated usar extension point para agregar las acciones al menú
	 * @param menu
	 */
	@Deprecated
	protected abstract void populateMenu(IMenuManager menu);

	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		MenuManager mm = createMenuManager();
		populateMenu0(mm);
		contribute(menuManager, mm);
	}

	private void populateMenu0(MenuManager mm) {
		populateMenu(mm);
		for (ActionContribution<T> actionc : actions) {
			if (actionc.onMenu())
				add(mm, actionc);
		}
	}

	private void add(IContributionManager mm, ActionContribution<T> actionc) {
		mm.add(wrap(actionc.action()));
	}

	@Override
	public void contributeToCoolBar(ICoolBarManager coolBarManager) {
		ToolBarManager tbm = new ToolBarManager();
		for (ActionContribution<T> actionc : actions) {
			if (actionc.onToolbar())
				add(tbm, actionc);
		}
		coolBarManager.add(tbm);
	}

	/**
	 * 
	 * Lee los extension points que se hayan registrado y los procesa
	 * 
	 */
	private void populateByExtensionPoint(final List<ActionContribution<T>> actions) {
		ExtensionPointLoader.create("edu.medicine.bioplat.rcp.editor.operation.contribution").load(new DefaultExtensionLoaderRunnable() {
			@Override
			protected void runOn(IConfigurationElement celement) throws Exception {
				ActionContribution<T> ac = (ActionContribution<T>) celement.createExecutableExtension("class");
				ac.modelProvider(AbstractEditorActionBarContributor.this);
				ac.caption(celement.getAttribute("caption"));
				ac.image(Activator.imageDescriptorFromPlugin(celement.getAttribute("image")));
				ac.onMenu(Boolean.valueOf(celement.getAttribute("onMenu")));
				ac.onToolbar(Boolean.valueOf(celement.getAttribute("onToolbar")));

				IConfigurationElement[] c = celement.getChildren("selection");
				final Class<?> configuredSelectionClass = Class.forName(c[0].getAttribute("class"));
				boolean shouldAdd = (c.length == 0) || //
						configuredSelectionClass.isAssignableFrom(getSelectionType());

				if (shouldAdd) {
					// mm.add(wrap(ac.action()));
					actions.add(ac);
				}
			}

		});
	}

	protected Class<?> getSelectionType() {
		return Object.class;
	}

	private void contribute(IMenuManager menuManager, MenuManager mm) {
		menuManager.prependToGroup("entidad.additions", mm);
	}

	private MenuManager createMenuManager() {
		return new MenuManager("Operations");
	}

	/**
	 * Devuelve el model para cuyo editor se contribuye
	 * 
	 * @return
	 */
	@Override
	public T model() {
		final IEditorPart activeEditor = getPage().getActiveEditor();
		Assert.isNotNull(activeEditor, "Early attempt to access to editor. It is not created yet.");
		return ((ModelProvider<T>) activeEditor).model();
	}

	private static IAction wrap(IAction action) {
		action = AsyncAction.wrap(action);
		action = MessageViewOpenAction.wrap(action);
		return action;
	}
}
