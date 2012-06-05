package edu.unlp.medicine.bioplat.rcp.ui.genes.view.parser;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;

public class GeneUrlParser {

	private static final String TITLE_URL_SEPARATOR = "::";
	private static final char URLS_SEPARATOR = '|';
	public static final String GEN_ID_HOLDER = "{genId}";

	public static List<GeneUrl> parse(String genesUrls) {

		List<GeneUrl> result = Lists.newArrayList();

		String[] urls = StringUtils.split(genesUrls, URLS_SEPARATOR);
		for (String url : urls) {
			String[] strparsed = StringUtils.splitByWholeSeparator(url, TITLE_URL_SEPARATOR);
			if (strparsed.length == 2)
				result.add(new GeneUrl(strparsed[0], strparsed[1]));
			else
				MessageManager.INSTANCE.add(Message.error(url + " is not a valid url for a gene. Correct sintax is title::weburl+parameters "));
		}

		return result;

	}

}
