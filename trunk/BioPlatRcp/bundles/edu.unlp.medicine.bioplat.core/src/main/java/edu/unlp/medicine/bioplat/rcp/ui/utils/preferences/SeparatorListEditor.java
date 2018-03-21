package edu.unlp.medicine.bioplat.rcp.ui.utils.preferences;

import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

/**
 * Implementación por default para manejar lista de strings en las páginas de
 * preferencias
 * 
 * @author diego martínez
 * 
 */
public class SeparatorListEditor extends EntryModifiableListEditor {
	public SeparatorListEditor(String name, String label, final Composite parent) {
		super(name, label, parent);

		getListControl(parent).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {

				// TODO sacar el null, por ahora se puede usar porque el evento
				// no es usado en el selection listener configurado en el botón
				// de edición
				getEditButton().notifyListeners(SWT.Selection, null);

			}
		});

	}

	@Override
	protected String[] parseString(String stringList) {

		StringTokenizer st = new StringTokenizer(stringList, separator());//$NON-NLS-1$
		List<String> v = Lists.newArrayList();
		while (st.hasMoreElements()) {
			v.add(st.nextElement().toString());
		}
		return (String[]) v.toArray(new String[v.size()]);
	}

	@Override
	protected String getNewInputObject() {
		InputDialog inputdialog = new InputDialog(PlatformUIUtils.findShell(), dialogTitle(), dialogMessage(), initialValue(), createValidator());
		if (inputdialog.open() == InputDialog.OK)
			return inputdialog.getValue();
		else
			return null;
	}

	protected String initialValue() {
		return null;
	}

	protected String dialogMessage() {
		return "";
	}

	protected String dialogTitle() {
		return "New";
	}

	protected IInputValidator createValidator() {
		return new IInputValidator() {

			@Override
			public String isValid(String newText) {
				if (newText == null || newText.trim().isEmpty())
					return "Debe ingresar un valor válido";
				return null;
			}
		};
	}

	@Override
	protected String createList(String[] items) {
		StringBuffer sb = new StringBuffer();
		for (String item : items)
			sb.append(item).append(separator());
		return sb.toString();
	}

	protected String separator() {
		return "|";
	}

	@Override
	protected String getModifiedEntry(String original) {
		InputDialog entryDialog = new InputDialog(PlatformUIUtils.findShell(), editionDialogTitle(), editDialogMessage(), original, createValidator());
		if (entryDialog.open() == InputDialog.OK) {
			return entryDialog.getValue();
		}
		return original;
	}

	protected String editDialogMessage() {
		return dialogMessage();
	}

	protected String editionDialogTitle() {
		return "Edit";
	}
}
