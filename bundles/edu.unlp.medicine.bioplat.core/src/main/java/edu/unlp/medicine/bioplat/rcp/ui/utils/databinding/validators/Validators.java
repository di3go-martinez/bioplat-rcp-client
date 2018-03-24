package edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class Validators {
	public static IValidator and(final IValidator... validators) {
		return new IValidator() {

			@Override
			public IStatus validate(final Object value) {
				for (IValidator validator : validators) {
					IStatus status = validator.validate(value);
					if (!status.isOK())
						return status;
				}
				return ValidationStatus.ok();
			}
		};
	}
}
