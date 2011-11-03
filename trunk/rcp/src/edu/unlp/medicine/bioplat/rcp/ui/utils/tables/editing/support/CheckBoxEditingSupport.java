package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

public class CheckBoxEditingSupport extends EditingSupport {


    public CheckBoxEditingSupport(TableViewer viewer) {
        super(viewer);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        return new CheckboxCellEditor(((TableViewer) getViewer()).getTable());
    }

    @Override
    protected boolean canEdit(Object element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected Object getValue(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setValue(Object element, Object value) {
        // TODO Auto-generated method stub

    }

}
