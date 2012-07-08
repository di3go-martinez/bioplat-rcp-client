package edu.unlp.medicine.bioplat.rcp.utils.wizards;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class WizardPageUtils {
	private WizardPageUtils() {
	}

	/**
	 * Crea en el container un Label y un Text
	 * 
	 * @param container
	 * @param dbc
	 * @param model
	 * @param label
	 * @return
	 */
	public static BindingResult createText(Composite container, DataBindingContext dbc, IObservableValue model, String label) {
		new Label(container, SWT.NONE).setText(label);
		Text t = new Text(container, SWT.BORDER);
		t.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		Binding b = dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), model);
		return new BindingResult(b);
	}
}
