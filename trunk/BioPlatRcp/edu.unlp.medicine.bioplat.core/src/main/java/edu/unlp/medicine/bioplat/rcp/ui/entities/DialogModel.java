package edu.unlp.medicine.bioplat.rcp.ui.entities;

import org.eclipse.jface.dialogs.Dialog;
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
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = Widgets.createDefaultContainer((Composite) super.createDialogArea(parent));
		Widgets.createText(container, modelprovider.model(), "name").readOnly();
		return container;
	}

}
