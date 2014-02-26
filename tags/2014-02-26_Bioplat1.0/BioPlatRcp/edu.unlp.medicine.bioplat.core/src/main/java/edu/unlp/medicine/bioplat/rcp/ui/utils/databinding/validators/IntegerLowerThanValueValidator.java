package edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators;

import java.io.File;
import java.util.List;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import com.google.common.collect.Lists;

/**
 * 
 * 
 * Valida: error si el nombre de archivo es un directory; warning si el archivo
 * ya existe; ok en otro caso
 * 
 * @author diego
 * 
 */

public class IntegerLowerThanValueValidator implements IValidator {

	int max; 
	String error;
	String field;
	
	



	public IntegerLowerThanValueValidator(int max, String error, String field) {
		super();
		this.max = max;
		this.error = error;
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}


	@Override
	public IStatus validate(Object value) {
		if (value==null) return  ValidationStatus.error(field + " is required");
		int valueInt = (Integer)value;
		if (max<valueInt ) return ValidationStatus.error(error);
		else return ValidationStatus.ok();
	}

}