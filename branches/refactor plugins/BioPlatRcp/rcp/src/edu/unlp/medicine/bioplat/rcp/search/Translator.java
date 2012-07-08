package edu.unlp.medicine.bioplat.rcp.search;

import org.hibernate.criterion.Criterion;

public interface Translator {

	Criterion translateEqual(ValueSearchControl sc);

	Criterion translateBetween(RangeSearchControl sc);
}
