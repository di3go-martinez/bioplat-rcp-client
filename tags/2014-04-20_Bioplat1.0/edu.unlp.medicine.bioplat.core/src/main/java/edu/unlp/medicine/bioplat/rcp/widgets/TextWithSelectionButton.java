package edu.unlp.medicine.bioplat.rcp.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Text;

/**
 * widget que define un campo de texto con un botón que abre un diálogo que
 * permite seleccionar algo
 * 
 * @author diego
 * 
 */
public abstract class TextWithSelectionButton {

	protected Text text;

	protected abstract Dialog createDialog(Composite parent);

	protected Button b;

	public TextWithSelectionButton(Composite parent) {
		this(parent, SWT.NONE);
	}

	public TextWithSelectionButton(Composite parent, int style) {
		Composite c = new Composite(parent, style);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).create());
		c.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		createDialog(parent);

		text = new Text(c, SWT.BORDER);
		text.setLayoutData(GridDataFactory.fillDefaults().align(SWT.FILL, GridData.CENTER).grab(true, false).create());

		b = new Button(c, SWT.FLAT);
		b.setText("...");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String filename = getSelection();
				text.setText((filename == null) ? "" : filename);
			}
		});
	}

	public Control textControl() {
		return text;
	}

	public void bind(DataBindingContext dbc, IObservableValue ovalue) {
		dbc.bindValue(SWTObservables.observeText(text, SWT.Modify), ovalue);
	}

	protected abstract String getSelection();
}