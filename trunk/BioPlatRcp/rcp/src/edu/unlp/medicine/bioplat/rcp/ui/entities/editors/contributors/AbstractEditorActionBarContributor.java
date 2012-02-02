package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorActionBarContributor;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.actions.MessageViewOpenAction;
import edu.unlp.medicine.bioplat.rcp.ui.utils.extension.points.DefaultExtensionLoaderRunnable;
import edu.unlp.medicine.bioplat.rcp.ui.utils.extension.points.ExtensionPointLoader;
import edu.unlp.medicine.entity.generic.AbstractEntity;

//TODO no se usa para nada todavía el tipo genérico T, ver si se puede usar para el selectionType por ejemplo
//TODO agregar configuración en populateMenu, por ejemplo, para agregar una operación al toolbar, al menu (por default)
//TODO agregar método para permitir la ejecución en el threadui...
public abstract class AbstractEditorActionBarContributor<T> extends EditorActionBarContributor implements ModelProvider {

	/**
	 * Se agregan las operaciones a realizar sobre una entidad al menu
	 * 
	 * @param menu
	 */
	protected abstract void populateMenu(IMenuManager menu);

	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		MenuManager mm = createMenuManager();
		populateMenu0(mm);
		contribute(menuManager, mm);
	}

	private void populateMenu0(MenuManager mm) {
		populateMenu(mm);
		populateByExtensionPoint(mm);
	}

	/**
	 * 
	 * Lee los extension points que se hayan registrado y los procesa
	 * 
	 * @param mm
	 *            menú
	 */
	private void populateByExtensionPoint(final MenuManager mm) {
		ExtensionPointLoader.create("edu.medicine.bioplat.rcp.editor.operation.contribution").load(new DefaultExtensionLoaderRunnable() {
			@Override
			protected void runOn(IConfigurationElement celement) throws Exception {
				ActionContribution ac = (ActionContribution) celement.createExecutableExtension("class");
				ac.modelProvider(AbstractEditorActionBarContributor.this);
				ac.caption(celement.getAttribute("caption"));

				IConfigurationElement[] c = celement.getChildren("selection");
				final Class<?> configuredSelectionClass = Class.forName(c[0].getAttribute("class"));
				boolean shouldAdd = (c.length == 0) || //
						configuredSelectionClass.isAssignableFrom(getSelectionType());

				if (shouldAdd)
					mm.add(wrap(ac.action()));
			}

			private IAction wrap(IAction action) {
				// TODO enable!
				// action = AsyncAction.wrap(action);
				action = MessageViewOpenAction.wrap(action);

				return action;
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
		return new MenuManager("Operaciones");
	}

	/**
	 * Devuelve el model para cuyo editor se contribuye
	 * 
	 * @return
	 */
	@Override
	public <T extends AbstractEntity> T model() {
		final IEditorPart activeEditor = getPage().getActiveEditor();
		Assert.isNotNull(activeEditor, "Early attempt to access to editor. It is not created yet.");
		return ((ModelProvider) activeEditor).model();
	}

}
