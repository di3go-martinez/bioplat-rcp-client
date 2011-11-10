package edu.unlp.medicine.bioplat.rcp.ui.utils.accesors;

import ognl.Ognl;
import ognl.OgnlException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OgnlAccesor implements Accesor {

    private static Logger logger = LoggerFactory.getLogger(OgnlAccesor.class);

	private String propertyPath;


	private OgnlAccesor(String propertyPath) {
		this.propertyPath = propertyPath;
	}

	public static Accesor createFor(String propertyPath) {
        return new NullSafeAccesor(new OgnlAccesor(propertyPath), "");
	}

	@Override
	public Object get(Object element) {
		try {
			return Ognl.getValue(propertyPath, element);
		} catch (OgnlException e) {
            logger.error("Accessing to " + propertyPath + " on" + element.getClass(), e);
		}
		return null;
	}

	@Override
	public void set(Object element, Object value) {
		try {
			Ognl.setValue(propertyPath, element, value);
		} catch (OgnlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
