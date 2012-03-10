package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public abstract class AbstractActionContribution<T extends AbstractEntity> extends Action implements ActionContribution<T> {

	private ModelProvider<T> modelProvider;

	@Override
	public IAction action() {
		return this;
	}

	@Override
	public void modelProvider(ModelProvider<T> modelProvider) {
		this.modelProvider = modelProvider;
	}

	protected T model() {
		return modelProvider.model();
	}

	@Override
	public void caption(String caption) {
		setText(caption);
	}

	@Override
	public abstract void run();
}
