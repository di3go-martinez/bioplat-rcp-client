package edu.unlp.medicine.bioplat.rcp.search;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class HibernateTranslator implements Translator {

	@Override
	public Criterion translateEqual(ValueSearchControl sc) {
		return Restrictions.eq(sc.getPropertyPath(), sc.getValue());
	}

	@Override
	public Criterion translateBetween(RangeSearchControl sc) {
		return Restrictions.between(sc.getPropertyPath(), sc.getLow(), sc.getHi());
	}

	@Override
	public String toString() {
		return "Translator using Restrictions";
	}

}
