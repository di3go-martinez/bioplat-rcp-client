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

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.genes.GenesPluginActivator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.preferences.ExternalURLInformationPage;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUtils;

public class InitializeGeneSetUrlStartup implements IStartup {
	private static Logger logger = LoggerFactory.getLogger(InitializeGeneSetUrlStartup.class);
	private static final String DEFAULT_GENE_SIGNATURE_URLS_FILE = "default_geneSignature_urls_file";

	@Override
	public void earlyStartup() {

		File f;
		if (!(f = new File(".geneSigUrlsInitialized")).exists()) {
			String[] defaults = fillDefaults();
			String urls = StringUtils.join(defaults, '|');
			PlatformUtils.preferences(Activator.id()).put(ExternalURLInformationPage.EXTERNAL_URLS, urls);
			// TODO acomodar mejor
			ScopedPreferenceStore prefs = new ScopedPreferenceStore(ConfigurationScope.INSTANCE, GenesPluginActivator.id());
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

	public static String[] fillDefaults() {
		List<String> result = Lists.newArrayList();
		Scanner scanner;
		try {
			final String filepath = System.getProperty(DEFAULT_GENE_SIGNATURE_URLS_FILE);
			if (filepath == null) {
				logger.warn("No está configurada la variable " + DEFAULT_GENE_SIGNATURE_URLS_FILE + ". No se inicializarán las urls por default");
				return new String[0];
			}
			scanner = new Scanner(new File(filepath));
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
