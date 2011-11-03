package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TableViewer;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;

public class NumberEditingSupport extends AbstractEditingSupport {

	public NumberEditingSupport(TableViewer viewer, Accesor accesor) {
		super(viewer, accesor);
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
