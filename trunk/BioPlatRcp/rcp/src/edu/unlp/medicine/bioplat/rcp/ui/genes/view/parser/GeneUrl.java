package edu.unlp.medicine.bioplat.rcp.ui.genes.view.parser;

import edu.unlp.medicine.entity.gene.Gene;

public class GeneUrl {

	private static final String GEN_HOLDER = "{genId}";

	public GeneUrl(String title, String url) {
		this.title = title;
		this.url = url;
	}

	private String url, title;

	public String url() {
		return url;
	}

	public String url(Gene gene) {
		return convertRestUrl(url, gene);
	}

	public String title() {
		return title;
	}

	private String convertRestUrl(String restUrl, Gene input) {
		return restUrl.replace(GEN_HOLDER, input.getEntrezIdAsString());
	}
}
