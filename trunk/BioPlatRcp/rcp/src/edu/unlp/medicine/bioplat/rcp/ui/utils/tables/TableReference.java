package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Table;

public interface TableReference {

	/**
	 * refresco explícito de la tabla
	 */
	void refresh();

	/**
	 * 
	 * @param input
	 * @beta requiere un mejor análisis
	 */
	void input(List input);

	// TODO
	// void input(AbstractEntity model);

	void add(Object element);

	@Deprecated
	ColumnManager columnManager();

	/**
	 * borra una fila de la tabla
	 * 
	 * @param data
	 */
	void remove(Object data);

	void removeSelected();

	void show(Object element);

	List selectedElements();

	List focusedElements();

	void addSelectionChangeListener(ISelectionChangedListener listener);

	/**
	 * Da acceso a la tabla interna.... usar solo en casos que se requiera algún
	 * detalle particular
	 * 
	 * @return
	 */
	Table getTable();
}
