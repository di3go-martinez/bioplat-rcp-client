package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells;

interface CellValueResolver {
	void doSet(Object value);

	Object doGet();
}