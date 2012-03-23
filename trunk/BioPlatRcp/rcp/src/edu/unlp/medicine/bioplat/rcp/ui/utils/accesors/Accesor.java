package edu.unlp.medicine.bioplat.rcp.ui.utils.accesors;

public interface Accesor {

	/**
	 * 
	 * @param element
	 * @return devuelve algun <i>rasgo</i> de element
	 */
	Object get(Object element);

	/**
	 * en algún <i>rasgo</i> del objeto element, se setea value
	 * 
	 * @param element
	 * @param value
	 */
	void set(Object element, Object value);
}
