package edu.unlp.medicine.bioplat.rcp.core.selections;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

import com.google.common.collect.Maps;

/**
 * Almacena múltiples selecciones bajo alguna clave
 * 
 * @author diego
 * 
 */
public class MultipleSelection implements ISelection {

	private Map<Object, IStructuredSelection> selections = Maps.newHashMap();

	@Override
	public boolean isEmpty() {
		for (ISelection s : selections.values())
			if (!s.isEmpty())
				return false;
		return true;
	}

	public MultipleSelection put(Object key, IStructuredSelection selection) {
		selections.put(key, selection);
		return this;
	}

	public MultipleSelection remove(Object key) {
		selections.remove(key);
		return this;
	}

	public IStructuredSelection get(Object key) {
		IStructuredSelection result = selections.get(key);
		if (result == null)
			result = StructuredSelection.EMPTY;
		return result;
	}

	public MultipleSelection add(Map<Object, IStructuredSelection> additionalSelections) {
		for (Entry<Object, IStructuredSelection> e : additionalSelections.entrySet())
			put(e.getKey(), e.getValue());
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("MultipleSelection { ");
		for (Entry<Object, IStructuredSelection> s : selections.entrySet())
			sb.append(s.getKey() + " => " + s.getValue() + "; ");
		sb.replace(sb.length() - 2, sb.length(), " }");
		return sb.toString();
	}

}
