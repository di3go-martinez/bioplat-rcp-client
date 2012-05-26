package edu.unlp.medicine.bioplat.rcp.ui.genes.view.parser;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

public class GeneUrlParser {

	private static final String TITLE_URL_SEPARATOR = "::";
	private static final char URLS_SEPARATOR = '|';
	public static final String GEN_ID_HOLDER = "{genId}";

	public static List<GeneUrl> parse(String genesUrls) {

		List<GeneUrl> result = Lists.newArrayList();

		String[] urls = StringUtils.split(genesUrls, URLS_SEPARATOR);
		for (String url : urls) {
			String[] strparsed = StringUtils.splitByWholeSeparator(url, TITLE_URL_SEPARATOR);
			result.add(new GeneUrl(strparsed[0], strparsed[1]));
		}

		return result;

	}

}
