package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TableViewer;

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

}
