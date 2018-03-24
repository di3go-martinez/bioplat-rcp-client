package edu.unlp.medicine.bioplat.rcp.ui.genes.view.acions;

import org.eclipse.jface.dialogs.Dialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.dialogs.IntegerInputDialog;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * 
 * @author diego
 * @deprecated migrar a handler
 * 
 *             FIXME está mal hecha... se instancia dos veces por editor...
 */
// TODO extends y/o implements
@Deprecated
public class AddRandomGenesAction extends AbstractActionContribution<Biomarker> {

	private static Logger logger = LoggerFactory.getLogger(AddGenesAC.class);
	@Override
	public void run() {

		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				MessageManager mm = MessageManager.INSTANCE;

				Biomarker b = getActiveBiomarker();
				if (b == null) {
					mm.add(Message.warn("There is no gene signature selected"));
					return;
				}

				
				
				//GenesInputDialog mydialog = new GenesInputDialog(PlatformUIUtils.findShell());
				IntegerInputDialog mydialog = new IntegerInputDialog(PlatformUIUtils.findShell());
				if (mydialog.open() == Dialog.OK){
						//int number = Integer.valueOf(mydialog.getids()[0]);
					int number = Integer.valueOf(mydialog.getNumber());
					model().addRandomGenes(number);
					mm.add(Message.info(number + " random genes were added to " + model().getName() + " GeneSignature"));
				}
			}
	     });		

	}

	private Biomarker getActiveBiomarker() {
		return model();
	}



}
