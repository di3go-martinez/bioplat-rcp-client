package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import org.eclipse.jface.action.IAction;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;

public interface ActionContribution {
	void modelProvider(ModelProvider modelProvider);

	IAction action();

}
