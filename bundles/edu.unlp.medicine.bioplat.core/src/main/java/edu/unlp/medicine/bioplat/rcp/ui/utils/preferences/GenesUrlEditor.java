package edu.unlp.medicine.bioplat.rcp.ui.utils.preferences;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * Agrega chequeo del formato de las urls ingresadas
 * 
 * @author diego martínez
 * 
 */
// TODO mover al plugin de genes o cambiar el nombre y extender en genes
public class GenesUrlEditor extends SeparatorListEditor {

	private String variables;

	public GenesUrlEditor(String name, String label, Composite parent, String variables) {
		super(name, label, parent);
		this.variables = variables;
	}

	@Override
	protected IInputValidator createValidator() {
		final IInputValidator original = super.createValidator();
		return new IInputValidator() {

			@Override
			public String isValid(String newText) {
				String result = original.isValid(newText);
				if (result != null)
					return result;
				else
					return (isValidFormat(newText) ? null : "El formato no es válido");
			}

			private boolean isValidFormat(String newText) {
				return newText.matches(regexp());
			}

		};
	}

	protected String regexp() {
		return ".*::.*[|.*::.*]*";
	}

	@Override
	protected String dialogMessage() {
		return "Format: ProviderName::URL, where url can use the following variables: " + variables;
	}
}