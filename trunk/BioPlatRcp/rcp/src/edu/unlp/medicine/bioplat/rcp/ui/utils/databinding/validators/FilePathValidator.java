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
public class FilePathValidator implements IValidator {

	private FilePathValidator(List<Checker> checkers) {
		this.checkers = checkers;
	}

	/**
	 * @deprecated migrar a <i>"FluentInterface"</i>
	 */
	@Deprecated
	public FilePathValidator() {
		checkers = Lists.newArrayList();
	}

	@Override
	public IStatus validate(Object value) {

		File f = new File(value.toString());
		IStatus result = ValidationStatus.ok();
		if (!checkers.isEmpty()) {
			for (Checker chk : checkers)
				if (!chk.validate(f))
					result = chk.status();
		} else { // FIXME comportamiento por default... borrar cuando se migre
			if (f.exists())
				result = ValidationStatus.warning("El archivo ingresado ya existe, se sobreescribirá");
			if (f.isDirectory())
				result = ValidationStatus.error("El nombre de archivo ingresado ya existe como directorio");
		}
		return result;
	}

	public static FilePathValidator create() {
		List<Checker> l = Lists.newArrayList();
		return new FilePathValidator(l);
	}

	// filtros
	public FilePathValidator fileMustExist() {
		checkers.add(new Checker() {

			@Override
			public boolean validate(File f) {
				return f.exists() && f.isFile();
			}

			@Override
			public IStatus status() {
				return ValidationStatus.error("El archivo ingresado no es válido");
			}
		});
		return this;
	}

	private List<Checker> checkers;

	private interface Checker {
		boolean validate(File f);

		IStatus status();
	}
}