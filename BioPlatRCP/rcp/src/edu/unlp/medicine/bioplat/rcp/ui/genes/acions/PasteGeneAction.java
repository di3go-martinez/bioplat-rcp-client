package edu.unlp.medicine.bioplat.rcp.ui.genes.acions;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.bioplat.rcp.ui.utils.Models;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class PasteGeneAction extends Action {
	public PasteGeneAction() {
		setText("Paste!");
	}

	@Override
	public void run() {

		Biomarker b = Models.getInstance().getActiveBiomarker();
		if (b == null)
			return;

		mydialog mydialog = new mydialog(null);
		if (mydialog.open() == Dialog.OK)
			for (String id : mydialog.getids())
				b.addGene(MetaPlat.getInstance().getGeneById(id));

	}
}

class mydialog extends Dialog {

	private String separator = " ";

	protected mydialog(Shell parentShell) {
		super(parentShell);

	}

	public String[] getids() {
		return StringUtils.split(value, separator);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Pegar los genes separados por '" + separator + "'");
	}

	private String value;

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite result = (Composite) super.createDialogArea(parent);

		final Text text = new Text(result, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				value = text.getText();
			}
		});
		return result;
	}

}
