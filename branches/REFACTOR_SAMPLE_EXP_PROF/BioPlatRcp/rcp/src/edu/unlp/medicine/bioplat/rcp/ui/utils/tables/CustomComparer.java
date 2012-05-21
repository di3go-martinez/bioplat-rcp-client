package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import org.eclipse.jface.viewers.IElementComparer;

public class CustomComparer implements IElementComparer {

	@Override
	public boolean equals(Object a, Object b) {
		// TODO revisar por qué a veces a y b no son del mismo tipo
		return (a.getClass().equals(b.getClass())) && a.equals(b);
	}

	@Override
	public int hashCode(Object element) {
		// implementación default
		return element.hashCode();
	}

}
