package edu.unlp.medicine.bioplat.rcp.intro.actions;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

/**
 * Revisar si vale la pena usar este diálogo, en principio, surgió para
 * adminsitrar el tamaño del diálogo, para intentar mantener un mejor control
 * del tamaño (tanto cuando se abre el diálogo desde la welcome page o desde el
 * wizard que se dispara desde el menú)
 * 
 * @author diego
 * 
 */
@Deprecated
public class MyWizardDialog extends WizardDialog {

	public MyWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Point getInitialSize() {
		// see org.eclipse.ui.internal.handlers.WizardHandler.New.executeHandler
		return new Point(Math.max(/* SIZING_WIZARD_WIDTH */500, -1 * this.getShell().getSize().x), /* SIZING_WIZARD_HEIGHT */500);
	}

}
