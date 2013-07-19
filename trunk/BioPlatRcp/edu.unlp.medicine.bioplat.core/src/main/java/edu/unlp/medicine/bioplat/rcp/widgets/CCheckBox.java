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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationListener;
import edu.unlp.medicine.entity.generic.AbstractEntity;

/**
 * extensión del widget checkbox
 * 
 * @author diego martínez
 * 
 */
public class CCheckBox implements Widget {

	private Button checkbox;
	private String property;
	private Binding binding;

	CCheckBox(Composite parent, String label, AbstractEntity model, String property) {
		checkbox = new Button(parent, SWT.CHECK);
		checkbox.setText(label);
		configureDataBinding(model, property);
		configureDefaults();
	}

	private void configureDefaults() {
		checkbox.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		checkbox.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				removeCurrentBinding();
			}
		});
	}

	private void removeCurrentBinding() {
		DataBindingContextHolder.dataBindingGlobalContext().removeBinding(binding);
		binding.dispose();
	}

	private void configureDataBinding(AbstractEntity model, String property) {
		this.property = property;
		final IObservableValue om = BeansObservables.observeValue(model, property);
		final ISWTObservableValue ot = SWTObservables.observeSelection(checkbox);
		binding = DataBindingContextHolder.dataBindingGlobalContext().bindValue(ot, om);
	}

	@Override
	public void retarget(AbstractEntity newModel) {
		removeCurrentBinding();
		configureDataBinding(newModel, property);
	}

	@Override
	public Widget addModificationListener(final ModificationListener listener) {
		checkbox.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				listener.modify(null);
			}

		});
		return this;
	}

	@Override
	public Widget readOnly() {
		// TODO revisar si está bien que se deshabilite al hacer readonly. en
		// todo caso simular un mejor readonly (que no deshabilite el control)
		checkbox.setEnabled(false);
		return this;
	}

	@Override
	public Widget setLayoutData(Object layoutData) {
		checkbox.setLayoutData(layoutData);
		return this;
	}

}
