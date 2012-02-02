package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import org.eclipse.jface.action.IAction;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;

public interface ActionContribution {

	/**
	 * 
	 * Configura el model provider
	 * 
	 * @param modelProvider
	 *            Colaborador que sabe como resolver el modelo
	 */
	void modelProvider(ModelProvider modelProvider);

	/**
	 * @deprecated migrar a comandos
	 */
	@Deprecated
	IAction action();

	void caption(String caption);
}
