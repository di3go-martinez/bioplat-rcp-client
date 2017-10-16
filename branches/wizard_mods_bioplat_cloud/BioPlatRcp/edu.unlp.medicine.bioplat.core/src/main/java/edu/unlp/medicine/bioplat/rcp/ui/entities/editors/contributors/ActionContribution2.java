package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;


//TODO reemplazar ActionContribution. Esta interface agrega tipo fluent de interface 
public interface ActionContribution2<T> {

	/**
	 * 
	 * Configura el model provider
	 * 
	 * @param modelProvider
	 *            Colaborador que sabe como resolver el modelo
	 */
	ActionContribution2<T> modelProvider(ModelProvider<T> modelProvider);

	/**
	 * @deprecated migrar a comandos
	 */
	@Deprecated
	IAction action();

	ActionContribution2<T> caption(String caption);

	ActionContribution2<T> image(ImageDescriptor image);

	ActionContribution2<T> onMenu(Boolean present);

	boolean onMenu();

	ActionContribution2<T> onToolbar(Boolean present);

	boolean onToolbar();

	/**
	 * indica que la action pertenece a algún grupo
	 * 
	 * @param groupId
	 */
	ActionContribution2<T> group(String groupId);

	String group();
}
