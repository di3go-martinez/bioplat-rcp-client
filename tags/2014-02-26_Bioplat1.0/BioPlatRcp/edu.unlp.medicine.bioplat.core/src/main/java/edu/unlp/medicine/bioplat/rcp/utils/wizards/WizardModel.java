package edu.unlp.medicine.bioplat.rcp.utils.wizards;

import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.Beta;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.utils.Holder;

public class WizardModel {

	private Map<String, IObservableValue> values = Maps.newHashMap();
	private Map<String, Object> commonValues = Maps.newHashMap();

	public WizardModel add(String key, IObservableValue value) {
		values.put(key, value);
		return this;
	}

	public <T> WizardModel add(String key, Class<T> type, T initialValue) {
		return add(key, new WritableValue(initialValue, type));
	}

	public WizardModel add(String key) {
		return add(key, new WritableValue());
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws org.eclipse.core.runtime.AssertionFailedException
	 *             assertion failed: Getter called outside realm...
	 */
	public <T> T value(final String key) {
		IObservableValue ov = valueHolder(key);
		if (ov != null)
			return (T) ov.getValue();// TODO ensureDoingInRealm(ov);
		return (T) commonValues.get(key);
	}

	private <T> T ensureDoingInRealm(final IObservableValue ov) {
		final Holder<T> holder = Holder.create();

		ov.getRealm().exec(new Runnable() {

			@Override
			public void run() {
				holder.hold((T) ov.getValue());
			}
		});

		return holder.value();
	}

	public List<String> keys() {
		List<String> result = Lists.newArrayList(values.keySet());
		result.addAll(commonValues.keySet());
		return result;
	}

	public IObservableValue valueHolder(String key) {
		// TODO!! probar para ver si se puede resolver el realm.... sin estar
		// dentro para hacer el cálculo del si entrar o no...
		// Shell shell = new Shell(Display.getCurrent());
		// Realm realm = SWTObservables.getRealm(shell.getDisplay());

		// if (!values.containsKey(key))
		// throw new RuntimeException("No se agregó el observable para " + key);
		final IObservableValue iObservableValue = values.get(key);
		final Object value = commonValues.get(key);
		// TODO analizar de agregarlos por default (add(key))... igualmente
		// tirar el warning... pros y constras
		if (iObservableValue == null && value == null)
			logger.warn("The holder for the key " + key + " is null");
		return iObservableValue;
	}

	// TODO analizar...
	public WizardModel set(String key, Object value) {
		commonValues.put(key, value);
		return this;
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @deprecated usar value(key)
	 */
	@Deprecated
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Wizard Model {  ");
		for (String key : keys())
			sb.append(key).append(" => ").append(value(key)).append(", ");
		sb.replace(sb.length() - 2, sb.length(), " }");
		return sb.toString();

	}

	private static Logger logger = LoggerFactory.getLogger(WizardModel.class);

	@Beta
	public void replace(String key, WritableValue writableValue) {
		values.put(key, writableValue);

	}
}
