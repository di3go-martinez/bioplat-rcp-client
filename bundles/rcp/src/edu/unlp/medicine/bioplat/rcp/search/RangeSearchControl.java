package edu.unlp.medicine.bioplat.rcp.search;

import org.hibernate.criterion.Criterion;

public class RangeSearchControl extends SearchControl {

	private Object low, hi;

	public Object getLow() {
		return low;
	}

	public RangeSearchControl setLow(Object low) {
		this.low = low;
		return this;
	}

	public Object getHi() {
		return hi;
	}

	public RangeSearchControl setHi(Object hi) {
		this.hi = hi;
		return this;
	}

	@Override
	public Criterion translate(Translator t) {
		return t.translateBetween(this);
	}

}
