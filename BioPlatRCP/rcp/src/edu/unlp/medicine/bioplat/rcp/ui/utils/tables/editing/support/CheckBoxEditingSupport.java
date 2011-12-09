package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;

public class CheckBoxEditingSupport extends AbstractEditingSupport {

	public CheckBoxEditingSupport(TableViewer viewer, Accesor accesor) {
		super(viewer, accesor);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		// return new BooleanCellEditor(((TableViewer) getViewer()).getTable());
		return new CheckboxCellEditor(((TableViewer) getViewer()).getTable());
	}

}
