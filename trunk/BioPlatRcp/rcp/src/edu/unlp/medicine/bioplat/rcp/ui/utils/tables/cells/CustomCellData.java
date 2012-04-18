package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;

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
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (!(obj instanceof CustomCellData)) {
			return false;
		}

		CustomCellData other = (CustomCellData) obj;
		Object o1 = this.resolver.doGet();
		Object o2 = other.resolver.doGet();
		return (o1 == null) ? o2 == null : o1.equals(o2);

	}

	// TODO revisar implementación
	@Override
	public int hashCode() {
		final Object o = resolver.doGet();
		if (o != null)
			return 32 * o.hashCode();
		else
			return 1;
	}

	@Override
	public String toString() {
		return "[" + getValue().toString() + "]";
	}
}