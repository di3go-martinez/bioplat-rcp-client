package edu.unlp.medicine.bioplat.rcp.search;

import org.hibernate.criterion.Criterion;

public abstract class SearchControl {

	private String propertyPath;

	public String getPropertyPath() {
		return propertyPath;
	}

	public SearchControl setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
		return this;
	}

	public abstract Criterion translate(Translator t);

}
