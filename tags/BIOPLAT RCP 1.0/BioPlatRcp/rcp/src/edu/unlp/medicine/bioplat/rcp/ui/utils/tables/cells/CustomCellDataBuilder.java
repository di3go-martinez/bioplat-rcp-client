package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells;

public class CustomCellDataBuilder {
	private CustomCellDataBuilder() {
	}

	public static CustomCellData create(CellValueResolver<?> resolver) {
		return new CustomCellData(resolver);
	}

	public static <T> CustomCellData constant(final T constantValue) {
		return new CustomCellData(new CellValueResolver<T>() {

			@Override
			public void doSet(T value) {
				// nothing, it's CONSTANT!
			}

			@Override
			public T doGet() {
				return constantValue;
			}
		});
	}
}
