package edu.unlp.medicine.bioplat.rcp.utils.wizards;

import java.util.Map;

import org.eclipse.core.databinding.observable.value.IObservableValue;

import com.google.common.collect.Maps;

public class WizardModel {

	private Map<String, IObservableValue> values = Maps.newHashMap();

	public WizardModel add(String key, IObservableValue value) {
		values.put(key, value);
		return this;
	}

	public <T> T value(final String key) {
		return (T) valueHolder(key).getValue();
	}

	public IObservableValue valueHolder(String key) {
		return values.get(key);
	}

}
