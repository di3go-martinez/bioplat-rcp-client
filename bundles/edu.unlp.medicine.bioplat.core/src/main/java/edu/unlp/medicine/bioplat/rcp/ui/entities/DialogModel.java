package edu.unlp.medicine.bioplat.rcp.ui.entities;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public class DialogModel<T extends AbstractEntity> extends Dialog {

	private ModelProvider<T> modelprovider;

	public DialogModel(Shell parentShell, ModelProvider<T> modelProvider) {
		super(parentShell);
		this.modelprovider = modelProvider;
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.RESIZE);
	}

	/**
	 * Las sublases deben redefinir y no es necesario que se invoque a este
	 * método
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = Widgets.createDefaultContainer((Composite) super.createDialogArea(parent));
		Widgets.createText(container, modelProvider().model(), "name");
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// creo solo el botón de ok
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	protected ModelProvider<T> modelProvider() {
		return modelprovider;
	}

	protected T model() {
		return modelProvider().model();
	}
}
