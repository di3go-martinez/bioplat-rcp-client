package edu.unlp.medicine.bioplat.rcp.widgets;

import java.util.Collection;
import java.util.EventListener;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationListener;
import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationTextEvent;
import edu.unlp.medicine.entity.generic.AbstractEntity;

/**
 * Extensión para el widget Text; estos widgets se crean mediante factories
 * 
 * @see Widgets#createText(Composite, Object, String)
 * @author Diego Martínez
 * @since 1.0
 */
public class CText implements Widget {

	private Text text;

	private Binding binding;

	private String property;

	CText(Composite parent, AbstractEntity model, String property) {
		this(parent, model, property, SWT.BORDER);
	}

	CText(Composite parent, AbstractEntity model, String property, int style) {
		text = new Text(parent, SWT.BORDER | style);

		configureDataBinding(model, property);

		configureDefaults();
	}

	private void configureDefaults() {
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		text.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				removeBinding();
			}
		});
	}

	private void removeBinding() {
		DataBindingContextHolder.dataBindingGlobalContext().removeBinding(binding);
		binding.dispose();
	}

	private void configureDataBinding(AbstractEntity model, String property) {
		this.property = property;
		final IObservableValue om = BeansObservables.observeValue(model, property);
		final ISWTObservableValue ot = SWTObservables.observeText(text, SWT.Modify);
		binding = DataBindingContextHolder.dataBindingGlobalContext().bindValue(ot, om);
	}

	@Override
	public void retarget(AbstractEntity model) {
		removeBinding();
		configureDataBinding(model, property);
	}

	public CText readOnly(boolean b) {
		text.setEditable(!b);
		return this;
	}

	@Override
	public Widget addModificationListener(final ModificationListener listener) {
		final ModifyListener modifyListener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				listener.modify(new ModificationTextEvent(event));
			}
		};
		Collection<EventListener> l = listeners.get(ModificationListener.class);
		l.add(modifyListener);
		text.addModifyListener(modifyListener);
		return this;
	}

	private Multimap<Class<?>, EventListener> listeners = ArrayListMultimap.create();

	@Override
	public Widget readOnly() {
		return readOnly(true);
	}

	@Override
	public Widget setLayoutData(Object layoutData) {
		text.setLayoutData(layoutData);
		return this;
	}
}
