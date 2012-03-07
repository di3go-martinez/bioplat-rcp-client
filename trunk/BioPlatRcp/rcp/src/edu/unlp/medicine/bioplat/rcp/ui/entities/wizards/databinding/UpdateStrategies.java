package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

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
					return new Status(IStatus.ERROR, "none", "Debe ingresar un valor para " + id);

				return Status.OK_STATUS;
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
