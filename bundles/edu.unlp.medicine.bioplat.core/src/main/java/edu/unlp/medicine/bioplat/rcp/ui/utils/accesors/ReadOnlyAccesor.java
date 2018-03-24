package edu.unlp.medicine.bioplat.rcp.ui.utils.accesors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ReadOnlyAccesor implements Accesor {

	private static Logger logger = LoggerFactory.getLogger(ReadOnlyAccesor.class);

	@Override
	public abstract Object get(Object element);

	@Override
	public final void set(Object element, Object value) {
		// throw new RuntimeException("Read only!");
		logger.warn("Intentando asignar un valor mediante un accesor de solo lectura. object -->" + element + "; value=" + value);
	}

}
