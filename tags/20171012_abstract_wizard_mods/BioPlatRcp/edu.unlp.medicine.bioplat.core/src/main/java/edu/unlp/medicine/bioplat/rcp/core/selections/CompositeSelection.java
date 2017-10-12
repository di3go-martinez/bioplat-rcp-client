package edu.unlp.medicine.bioplat.rcp.core.selections;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;

import com.google.common.collect.Lists;

/**
 * Almacena múltiples selecciones, por ejemplo: una entidad, elementos
 * seleccionados de esa u otra entidad, etc
 * 
 * @author diego
 * 
 */
public class CompositeSelection implements ISelection {

	private List<ISelection> selections = Lists.newArrayList();

	@Override
	public boolean isEmpty() {
		for (ISelection s : selections)
			if (!s.isEmpty())
				return false;
		return true;
	}

	public CompositeSelection add(ISelection selection) {
		selections.add(selection);
		return this;
	}

	public CompositeSelection remove(ISelection selection) {
		selections.remove(selection);
		return this;
	}

	public List<ISelection> selection() {
		return selections;
	}

	public void add(List<ISelection> additionalSelections) {
		selections.addAll(additionalSelections);
	}
}
