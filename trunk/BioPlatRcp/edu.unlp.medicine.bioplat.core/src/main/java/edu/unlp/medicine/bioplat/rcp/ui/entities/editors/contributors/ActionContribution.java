package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;

public interface ActionContribution<T> {

	/**
	 * 
	 * Configura el model provider
	 * 
	 * @param modelProvider
	 *            Colaborador que sabe como resolver el modelo
	 */
	void modelProvider(ModelProvider<T> modelProvider);

	/**
	 * @deprecated migrar a comandos
	 */
	@Deprecated
	IAction action();

	void caption(String caption);

	void image(ImageDescriptor image);

	void onMenu(Boolean present);

	boolean onMenu();

	void onToolbar(Boolean present);

	boolean onToolbar();

	/**
	 * indica que la action pertenece a algún grupo
	 * 
	 * @param groupId
	 */
	void group(String groupId);

	String group();
}
