package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;

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

	ColumnManager columnManager();

	void show(Object selectedGene);

	List selectedElements();

	List focusedElements();

	void addSelectionChangeListener(ISelectionChangedListener listener);
}
