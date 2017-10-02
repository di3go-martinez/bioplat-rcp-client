package edu.unlp.medicine.bioplat.rcp.ui.genes.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.dialogs.GenesInputDialog;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

public class OpenGeneViewAction extends AbstractActionContribution<Biomarker> {
	private static Logger logger = LoggerFactory.getLogger(OpenGeneViewAction.class);
	@Override
	public void run() {

		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {

				try {
					GeneViewPart view = (GeneViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("edu.medicine.bioplat.rcp.gene.view");
				} catch (PartInitException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
						
			}

			
		});

	}

	private Biomarker getActiveBiomarker() {
		return model();
	}

}
