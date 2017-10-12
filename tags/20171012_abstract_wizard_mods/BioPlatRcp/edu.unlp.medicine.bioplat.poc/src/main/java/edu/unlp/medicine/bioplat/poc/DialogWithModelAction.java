package edu.unlp.medicine.bioplat.poc;

import org.eclipse.swt.widgets.Display;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.DialogModel;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public class DialogWithModelAction extends AbstractActionContribution<AbstractEntity> {

	@Override
	public void run() {
		final ModelProvider<AbstractEntity> modelProvider = new ModelProvider<AbstractEntity>() {

			@Override
			public AbstractEntity model() {
				return DialogWithModelAction.this.model();
			}
		};
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new DialogModel(null, modelProvider).open();
			}
		});

	}
}
