package edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * 
 * Chequea si un valor existe en una entidad de chequeo.
 * 
 * @author diego
 * 
 */
public class RequiredValidator implements IValidator {

	private String message = "Debe ingresar un valor para ";
	private String entity = "";

	private RequiredValidator(String entityId) {
		entity = entityId;
	}

	/**
	 * 
	 * @param oid
	 *            un identificador de la entidad que se está chequeando
	 * @return
	 */
	public static IValidator create(String oid) {
		return new RequiredValidator(oid);
	}

	@Override
	public IStatus validate(Object value) {
		if (value == null || value.toString().isEmpty())
			return ValidationStatus.error(message + entity);
		else
			return ValidationStatus.ok();
	}

}
