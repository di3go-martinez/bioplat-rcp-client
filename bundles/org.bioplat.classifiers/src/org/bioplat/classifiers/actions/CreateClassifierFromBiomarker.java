package org.bioplat.classifiers.actions;

import org.bioplat.classifiers.dialogs.NewClassifierDialog;
import org.eclipse.jface.viewers.IStructuredSelection;

import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class CreateClassifierFromBiomarker extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {

		MultipleSelection selection = getSelection();
		IStructuredSelection validations = selection.get(Constants.SELECTED_VALIDATIONS);
		if (validations.isEmpty() || validations.size() > 1) {
			MessageManager.INSTANCE.add(Message.warn("Select one Statistical Validation"));
			return;
		}

		final Validation validation = (Validation) validations.getFirstElement();

		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				new NewClassifierDialog(validation).open();
			}

		});

	}

}
