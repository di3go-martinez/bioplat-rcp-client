package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells;

class CustomCellData {
	private CellValueResolver resolver;

	public CustomCellData(CellValueResolver resolver) {
		this.resolver = resolver;
	}

	public void setValue(Object value) {
		resolver.doSet(value);
	}

	public Object getValue() {
		return resolver.doGet();
	}
}