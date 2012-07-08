package edu.unlp.medicine.bioplat.rcp.ui.utils.accesors;

import ognl.Ognl;
import ognl.OgnlException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Accede a la propiedad de un objeto, propertyPath mediante
 * 
 * @author diego
 * 
 */
public class OgnlAccesor implements Accesor {

	private static Logger logger = LoggerFactory.getLogger(OgnlAccesor.class);

	private String propertyPath;
	// TODO es más rápido el acceso por property path preparseado?
	private Object parsedPropertyPath;

	private OgnlAccesor(String propertyPath) {
		this.propertyPath = propertyPath;
		try {
			parsedPropertyPath = Ognl.parseExpression(propertyPath);
		} catch (OgnlException e) {
			parsedPropertyPath = propertyPath;
		}
	}

	public static Accesor createFor(String propertyPath) {
		return new NullSafeAccesor(new OgnlAccesor(propertyPath), "");
	}

	@Override
	public Object get(Object element) {
		try {
			return Ognl.getValue(getPropertyPath(), element);
		} catch (OgnlException e) {
			logger.error("Accessing to " + propertyPath + " on" + element.getClass(), e);
		}
		return null;
	}

	private Object getPropertyPath() {
		return parsedPropertyPath;
	}

	@Override
	public void set(Object element, Object value) {
		try {
			Ognl.setValue(getPropertyPath(), element, value);
		} catch (OgnlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
