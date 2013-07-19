package edu.unlp.medicine.bioplat.rcp.ui.genes.view.parser;

import edu.unlp.medicine.entity.gene.Gene;

public class GeneUrl {

	private static final String GEN_HOLDER = "{genId}";
	private static final String GEN_NAME = "{genName}";
	private static final String GEN_ENSEMBLID = "{ensemblId}";

	/**
	 * 
	 * @param title
	 * @param url
	 *            es la url hacia un gen, puede contener placeholders tales como
	 *            genId, genName, ensemlId
	 */
	public GeneUrl(String title, String url) {
		this.title = title;
		this.url = url;
	}

	private String url, title;

	// public String url() {
	// return url;
	// }

	public String url(Gene gene) {
		return convertRestUrl(url, gene);
	}

	public String title() {
		return title;
	}

	private String convertRestUrl(String restUrl, Gene input) {
		return restUrl.replace(GEN_HOLDER, input.getEntrezIdAsString())//
				.replace(GEN_NAME, esc(input.getName()))//
				.replace(GEN_ENSEMBLID, input.getEnsemblId());
	}

	private CharSequence esc(String str) {
		if (str == null)
			return "";
		return str.replaceAll(" ", "%20");
	}
}
