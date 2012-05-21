package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

public interface DataTransformer<From, To> {

	To transform(From from);

	To nullValue();
}
