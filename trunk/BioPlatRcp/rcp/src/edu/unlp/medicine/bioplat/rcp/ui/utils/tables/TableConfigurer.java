//TODO mover de package
package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

public interface TableConfigurer {

	int limit();

	/**
	 * Arma el menú contextual de la grilla en función las acciones que tenga
	 * configuradas
	 */
	void configureMenu();
}
