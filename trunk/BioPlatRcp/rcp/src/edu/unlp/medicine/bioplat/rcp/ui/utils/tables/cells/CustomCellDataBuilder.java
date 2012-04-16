package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells;

class CustomCellDataBuilder {
	private CustomCellDataBuilder() {
		// TODO Auto-generated constructor stub
	}

	public static CustomCellData create(CellValueResolver resolver) {
		return new CustomCellData(resolver);
	}

	public static CustomCellData constant(final Object constantValue) {
		return new CustomCellData(new CellValueResolver() {

			@Override
			public void doSet(Object value) {
				// nothing, it's CONSTANT!

			}

			@Override
			public Object doGet() {
				return constantValue;
			}
		});
	}
}
