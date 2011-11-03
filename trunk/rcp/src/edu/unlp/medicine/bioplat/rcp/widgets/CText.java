package edu.unlp.medicine.bioplat.rcp.widgets;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.entity.generic.AbstractEntity;


/**
 * Extensi�n para el widget Text; estos widgets se crean mediante factories
 * 
 * @see Widgets#createText(Composite, Object, String)
 * @author Diego Mart�nez
 * @version $Revision:$
 * @updatedBy $Author:$ on $Date:$
 */
public class CText {

	private Text text;
	private Binding binding;

    <T extends AbstractEntity> CText(Composite parent, T model, String property) {
		this(parent, model, property, SWT.BORDER);
	}

    <T extends AbstractEntity> CText(Composite parent, T model, String property, int style) {
		text = new Text(parent, SWT.BORDER | style);

		configureDataBinding(model, property);

		configureDefaults();
	}

	private void configureDefaults() {
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		text.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
                DataBindingContextHolder.dataBindingGlobalContext().removeBinding(binding);
			}
		});
	}

	private <T> void configureDataBinding(T model, String property) {
		final IObservableValue om = BeansObservables.observeValue(model, property);
		final ISWTObservableValue ot = SWTObservables.observeText(text, SWT.Modify);
        binding = DataBindingContextHolder.dataBindingGlobalContext().bindValue(ot, om);
	}

    public void readOnly(boolean b) {
        text.setEditable(!b);
    }
}
