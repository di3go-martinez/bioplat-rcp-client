package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;

public abstract class AbstractEditingSupport extends EditingSupport {

	private Accesor accesor;
	private ColumnBuilder columnBuilder;

	public AbstractEditingSupport(TableViewer viewer, Accesor accesor, ColumnBuilder cb) {
		super(viewer);
		this.accesor = accesor;
		this.columnBuilder = cb;
	}

	protected Accesor accesor() {
		return this.accesor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true; // default
	}

	@Override
	protected final Object getValue(Object element) {
		return columnBuilder.transformer().transform(accesor().get(element));
	}

	@Override
	protected final void setValue(Object element, Object value) {
		accesor().set(element, value);
		// necesario porque si no no actualiza la vista
		// TODO ver qué propiedades de actualización se pueden setear
		getViewer().update(element, null);

	}

	@Override
	public TableViewer getViewer() {
		return (TableViewer) super.getViewer();
	}

	private CellEditor ce;

	@Override
	protected CellEditor getCellEditor(Object element) {
		if (ce == null)
			ce = createCellEditor();
		return ce;
	}

	private CellEditor createCellEditor() {
		ce = createCellEditor0();
		ce.setValidator(createCellEditorValidator());
		return ce;
	}

	protected CellEditor createCellEditor0() {
		return new TextCellEditor(getViewer().getTable(), SWT.NONE);
	}

	/**
	 * valido ok siempre por default...
	 * 
	 * @return
	 */
	protected ICellEditorValidator createCellEditorValidator() {
		return new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				return null;
			}
		};
	}

}
