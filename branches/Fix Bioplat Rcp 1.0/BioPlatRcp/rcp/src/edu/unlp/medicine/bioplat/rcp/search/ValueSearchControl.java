package edu.unlp.medicine.bioplat.rcp.search;

import org.hibernate.criterion.Criterion;

public class ValueSearchControl extends SearchControl {

	@Override
	public Criterion translate(Translator t) {
		return t.translateEqual(this);
	}

	private Object value;

	public Object getValue() {
		return value;
	}

	public ValueSearchControl setValue(Object value) {
		this.value = value;
		return this;
	}

}
