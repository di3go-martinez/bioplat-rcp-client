package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;

/**
 * 
 * Modelo para celdas "complejas"
 * 
 * @author Diego Martínez
 * @see CustomCellDataBuilder
 * @see CellValueResolver
 * 
 */
public class CustomCellData implements Accesor, Comparable<CustomCellData> {
	private CellValueResolver resolver;

	public CustomCellData(CellValueResolver resolver) {
		this.resolver = resolver;
	}

	public Object getValue() {
		return resolver.doGet();
	}

	public void setValue(Object value) {
		resolver.doSet(value);
	}

	@Override
	public int compareTo(CustomCellData o) {
		return CompareUtils.compareTo(this.resolver.doGet(), o.resolver.doGet());
	}

	@Override
	public Object get(Object element) {
		return resolver.doGet();
	}

	@Override
	public void set(Object element, Object value) {
		resolver.doSet(value);
	}

	@Override
	public String toString() {
		return "(" + getValue().toString() + ")";
	}
}