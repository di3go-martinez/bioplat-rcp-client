package edu.unlp.medicine.bioplat.rcp.search;

import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import edu.unlp.medicine.entity.gene.Gene;

public class Search {

	private List<SearchControl> controls;

	public Search(SearchControl... controls) {
		this.controls = Arrays.asList(controls);
	}

	public static void main(String[] args) {

		// SearchControl sc1 = new
		// ValueSearchControl().setValue(1).setPropertyPath("entrezId");
		SearchControl sc2 = new RangeSearchControl().setLow(1).setHi(10).setPropertyPath("entrezId");

		new Search(sc2).find(new HibernateTranslator());

	}

	private void find(Translator t) {

		DetachedCriteria dc = DetachedCriteria.forClass(Gene.class);
		for (SearchControl c : controls)
			dc.add(c.translate(t));

	}
}
