package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class UpdateStrategies {

	/**
	 * 
	 * @param id
	 *            una indicación del campo que se está chequeado
	 * @return
	 */
	public static UpdateValueStrategy nonNull(final String id) {
		UpdateValueStrategy result = new UpdateValueStrategy();
		result.setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				if (value == null)
					return ValidationStatus.error("Debe ingresar un valor para " + id); // new
																						// Status(IStatus.ERROR,
																						// "none",
																						// "Debe ingresar un valor para "
																						// +
																						// id);

				return ValidationStatus.ok();
			}
		});
		return result;
	}

	/**
	 * 
	 * @return null
	 */
	public static UpdateValueStrategy nullStrategy() {
		return null;
	}

}
