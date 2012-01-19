package edu.unlp.medicine.bioplat.rcp.utils;

import java.io.Serializable;

public class Holder<T> implements Serializable {
	private static final long serialVersionUID = -4487158007728361793L;
	private T value;

	public Holder(T initialValue) {
		this.value = initialValue;
	}

	public Holder() {
		this(null);
	}

	public T value() {
		return value;
	}

	public Holder<T> hold(T newvalue) {
		this.value = newvalue;
		return this;
	}

	@Override
	public String toString() {
		return "Holding: " + value();
	}
}
