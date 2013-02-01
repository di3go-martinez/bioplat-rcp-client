package edu.unlp.medicine.bioplat.rcp.ui.genes.startup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.IStartup;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.genes.GenesPluginActivator;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.preferences.ExternalGeneInformationPage;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUtils;

public class InitializeGenesUrlStartup implements IStartup {

	@Override
	public void earlyStartup() {

		File f;
		if (!(f = new File(".genesInitialized")).exists()) {
			String[] defaults = fillDefaults();
			String urls = StringUtils.join(defaults, '|');
			PlatformUtils.preferences(GenesPluginActivator.id()).put(ExternalGeneInformationPage.URLS, urls);
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static String[] fillDefaults() {
		List<String> result = Lists.newArrayList();
		Scanner scanner;
		try {
			scanner = new Scanner(new File(System.getProperty("default_genes_urls_file")));
			while (scanner.hasNextLine()) {
				String newUrl = scanner.nextLine();
				result.add(newUrl);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result.toArray(new String[0]);
	}
}
