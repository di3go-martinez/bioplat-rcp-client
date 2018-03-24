package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells;

public interface CellValueResolver<T> {

	void doSet(T value);

	T doGet();
}