package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;

public class NumberEditingSupport extends AbstractEditingSupport {

	public NumberEditingSupport(TableViewer viewer, Accesor accesor, ColumnBuilder cb) {
		super(viewer, accesor, cb);
	}

	@Override
	protected ICellEditorValidator createCellEditorValidator() {
		return new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				String result = null;
				try {
					Integer.valueOf(value.toString());
				} catch (Exception e) {
					result = e.getMessage();
				}
				return result;
			}
		};
	}

	@Override
	protected CellEditor createCellEditor0() {
		return new TextCellEditor(getViewer().getTable(), SWT.NONE) {
			@Override
			protected void doSetValue(Object value) {
				Assert.isTrue((text !=null && value instanceof Number));
				super.doSetValue(value.toString());
			}
		};
	}

}
