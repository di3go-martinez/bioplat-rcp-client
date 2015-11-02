package edu.unlp.medicine.bioplat.rcp.ui.genes.startup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.genes.GenesPluginActivator;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.preferences.ExternalGeneInformationPage;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUtils;

public class InitializeGenesUrlStartup implements IStartup {

	private static final String DEFAULT_GENES_URLS_FILE = "default_genes_urls_file";
	private static Logger logger = LoggerFactory.getLogger(InitializeGeneSetUrlStartup.class);

	@Override
	public void earlyStartup() {

		File f;
		if (!(f = new File(".genesInitialized")).exists()) {
			String[] defaults = fillDefaults();
			String urls = StringUtils.join(defaults, '|');
			PlatformUtils.preferences(GenesPluginActivator.id()).put(ExternalGeneInformationPage.URLS, urls);
			// TODO acomodar mejor
			ScopedPreferenceStore prefs = new ScopedPreferenceStore(ConfigurationScope.INSTANCE,
					GenesPluginActivator.id());
			try {
				prefs.save();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// TODO hacer private
	public static String[] fillDefaults() {
		List<String> result = Lists.newArrayList();
		Scanner scanner;
		try {
			final String filepath = System.getProperty(DEFAULT_GENES_URLS_FILE, null);
			if (!checks(filepath))
				return new String[0];
			scanner = new Scanner(new File(filepath));
			while (scanner.hasNextLine()) {
				String newUrl = scanner.nextLine();
				result.add(newUrl);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return result.toArray(new String[0]);
	}

	private static boolean checks(final String filepath) {
		if (filepath == null) {
			logger.warn("No está configurada la variable " + DEFAULT_GENES_URLS_FILE
					+ ". No se inicializarán las urls por default en la vista de genes");
			return false;
		}
		if (!new File(filepath).exists()) {
			logger.warn("El archivo '" + filepath + "' no existe.");
			return false;
		}
		return true;
	}
}
