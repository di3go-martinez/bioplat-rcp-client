package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.List;

public interface TableReference {

	/**
	 * refresco expl�cito de la tabla
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
}