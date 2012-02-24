package edu.unlp.medicine.bioplat.rcp.utils.wizards;

import java.util.Map;

import org.eclipse.core.databinding.observable.value.IObservableValue;

import com.google.common.collect.Maps;

public class WizardModel {

	private Map<String, IObservableValue> values = Maps.newHashMap();
	private Map<String, Object> commonValues = Maps.newHashMap();

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

	// TODO analizar...
	public WizardModel set(String key, Object value) {
		commonValues.put(key, value);
		return this;
	}

	// TODO analizar...
	public Object get(String key) {
		return get(key, null);
	}

	// TODO analizar...
	public Object get(String key, Object def) {
		Object result = commonValues.get(key);
		if (result == null)
			result = def;
		return result;
	}

}
