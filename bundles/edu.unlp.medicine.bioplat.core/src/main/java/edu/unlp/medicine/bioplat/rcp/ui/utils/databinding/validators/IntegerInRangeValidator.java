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
// TODO hacer configurable (¿vale la pena?)
public class IntegerInRangeValidator implements IValidator {

	int min, max; 
	String errorPorExcesoMayor, errorPorDebajoMenor, field;
	
	
	
	
	






	public IntegerInRangeValidator(int min, int max,
			String errorPorExcesoMayor, String errorPorDebajoMenor, String field) {
		super();
		this.min = min;
		this.max = max;
		this.errorPorExcesoMayor = errorPorExcesoMayor;
		this.errorPorDebajoMenor = errorPorDebajoMenor;
		this.field = field;
	}






	public String getField() {
		return field;
	}






	public void setField(String field) {
		this.field = field;
	}






	public int getMin() {
		return min;
	}






	public void setMin(int min) {
		this.min = min;
	}






	public int getMax() {
		return max;
	}






	public void setMax(int max) {
		this.max = max;
	}






	public String getErrorPorExcesoMayor() {
		return errorPorExcesoMayor;
	}






	public void setErrorPorExcesoMayor(String errorPorExcesoMayor) {
		this.errorPorExcesoMayor = errorPorExcesoMayor;
	}






	public String getErrorPorDebajoMenor() {
		return errorPorDebajoMenor;
	}






	public void setErrorPorDebajoMenor(String errorPorDebajoMenor) {
		this.errorPorDebajoMenor = errorPorDebajoMenor;
	}






	@Override
	public IStatus validate(Object value) {
		if (value==null) return  ValidationStatus.error(field + " is required");
		int valueInt = (Integer)value;
		if (min>valueInt) return ValidationStatus.error(errorPorDebajoMenor);
		if (max<valueInt) return ValidationStatus.error(errorPorExcesoMayor);
		else return ValidationStatus.ok();
		
	}

}