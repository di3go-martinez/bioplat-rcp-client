package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;

public abstract class AbstractEditingSupport extends EditingSupport {

	private Accesor accesor;

	public AbstractEditingSupport(TableViewer viewer, Accesor accesor) {
		super(viewer);
		this.accesor = accesor;
	}


    protected Accesor accesor() {
        return this.accesor;
    }

	@Override
	protected boolean canEdit(Object element) {
		return true; // default
	}


	@Override
	protected Object getValue(Object element) {
        return accesor().get(element).toString(); // TODO revisar lo del
												// toString()
	}

	@Override
	protected void setValue(Object element, Object value) {
        accesor().set(element, value);
		getViewer().refresh();// necesario porque si no no actualiza la vista
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
		ce = new TextCellEditor(getViewer().getTable(), SWT.NONE);
		ce.setValidator(createCellEditorValidator());
		return ce;
	}

	    /**
     * valido siempre por default...
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
