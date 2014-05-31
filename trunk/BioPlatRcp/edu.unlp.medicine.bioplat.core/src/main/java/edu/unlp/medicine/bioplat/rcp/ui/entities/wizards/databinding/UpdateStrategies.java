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
					return ValidationStatus.error("You must provide a value for " + id); // new
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

	private static interface Provider {
		IStatus validate(Object value);
	}

	private static UpdateValueStrategy byProvider(final Provider provider) {
		UpdateValueStrategy uvs = new UpdateValueStrategy();
		uvs.setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				return provider.validate(value);
			}
		});
		return uvs;
	}

	/**
	 * 
	 * @return null, ya que la API de databinding lo soporta
	 */
	public static UpdateValueStrategy nullStrategy() {
		return null;
	}

	// TODO revisar... no siempre son before...
	private static UpdateValueStrategy compose(final UpdateValueStrategy... strategies) {
		UpdateValueStrategy result = new UpdateValueStrategy();
		result.setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				for (UpdateValueStrategy uvs : strategies) {
					final IStatus status = uvs.validateBeforeSet(value);
					if (!(status.equals(ValidationStatus.ok())))
						return status;
				}
				return ValidationStatus.ok();
			}
		});
		return result;
	}
}
