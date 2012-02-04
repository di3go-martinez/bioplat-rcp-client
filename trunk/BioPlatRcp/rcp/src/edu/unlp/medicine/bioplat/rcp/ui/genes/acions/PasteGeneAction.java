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
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

/**
 * 
 * @author diego
 * @deprecated migrar a handler
 */
@Deprecated
public class PasteGeneAction extends Action {
	public PasteGeneAction() {
		setText("Paste!");
	}

	@Override
	public void run() {

		MessageManager mm = MessageManager.INSTANCE;

		Biomarker b = Models.getInstance().getActiveBiomarker();
		if (b == null) {
			mm.add(Message.warn("No hay biomarcador seleccionado"));
			return;
		}

		AddGenDialog mydialog = new AddGenDialog(null);
		if (mydialog.open() == Dialog.OK)
			for (String id : mydialog.getids()) {
				try {
					final Gene gene = MetaPlat.getInstance().getGeneById(id);
					if (!b.getGenes().contains(gene)) {
						b.addGene(gene);
						mm.add(Message.info("Se agregó el gen: " + gene));
					} else
						mm.add(Message.warn("El gen " + gene + " ya estaba agregado al biomarcador"));
				} catch (Exception e) {
					mm.add(Message.error("No se pudo agregar el gen con id: '" + id + "'", e));
				}
			}

	}
}

class AddGenDialog extends Dialog {

	private String separator = " ";

	protected AddGenDialog(Shell parentShell) {
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
		text.setToolTipText(getShell().getText());
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				value = StringUtils.replace(text.getText(), "\n", " ");
				value = StringUtils.replace(value, "\r", " ");
			}
		});
		return result;
	}

}
