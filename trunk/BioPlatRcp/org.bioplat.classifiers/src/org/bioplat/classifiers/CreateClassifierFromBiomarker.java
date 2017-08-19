package org.bioplat.classifiers;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class CreateClassifierFromBiomarker extends AbstractActionContribution<Biomarker> {

	@Override
	public void run() {
		MessageManager.INSTANCE.add(Message.info(
				"Nuevo clasificador a partir de los genes del biomarcador " + model() + ":" + model().getGenesAsList()));
	}

}
