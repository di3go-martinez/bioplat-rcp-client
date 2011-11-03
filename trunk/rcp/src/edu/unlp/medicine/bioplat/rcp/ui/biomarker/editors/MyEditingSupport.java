package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

@Deprecated
public class MyEditingSupport extends EditingSupport {

	private TableViewer tableViewer;

	public MyEditingSupport(TableViewer viewer) {
		super(viewer);
		tableViewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(tableViewer.getTable());
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
        return null// String.valueOf(((Gene) element).getExpresi�nG�nica())
        ;
	}

	@Override
	protected void setValue(Object element, Object value) {
        // try {
        // ((Gene) element).setExpresi�nG�nica(Long.valueOf(value.toString()));
        // tableViewer.refresh();// necesario porque si no no actualiza la
        // // vista
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
	}
}