package edu.unlp.medicine.bioplat.rcp.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;

/**
 * Abre un diálogo para seleccionar un directorio
 * 
 * @author diego
 * 
 */
public class DirectoryText extends TextWithSelectionButton {

	private DirectoryDialog dd;

	public DirectoryText(Composite parent) {
		this(parent, SWT.NONE);
	}

	public DirectoryText(Composite c, int style) {
		super(c, style);
	}

	@Override
	protected Dialog createDialog(Composite parent) {
		return dd = new DirectoryDialog(parent.getShell());
	}

	@Override
	protected String getSelection() {
		return dd.open();
	}

}
