package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

/**
 * Transformer para celdas abstracto
 * 
 * @author diego martínez
 * 
 * @param <From>
 *            tipo origen
 * @param <To>
 *            tipo destino
 */
public abstract class AbstractDataTransformer<From, To> implements DataTransformer<From, To> {

	@Override
	public final To transform(From from) {
		if (from == null)
			return nullValue();
		return doTransform(from);
	}

	protected abstract To doTransform(From from);

	@Override
	public To nullValue() {
		return null;
	}

}
