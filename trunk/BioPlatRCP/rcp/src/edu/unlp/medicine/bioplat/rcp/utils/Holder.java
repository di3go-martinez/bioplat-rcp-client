package edu.unlp.medicine.bioplat.rcp.utils;

import java.io.Serializable;

public class Holder<T> implements Serializable {
	private static final long serialVersionUID = -4487158007728361793L;
	private T value;

	public Holder(T initialValue) {
		super();
		this.value = initialValue;
	}

	public T value() {
		return value;
	}
	public T hold(T newvalue) {
		return this.value = newvalue;
	}

	@Override
	public String toString() {
		return "Holding: " + value();
	}
}
