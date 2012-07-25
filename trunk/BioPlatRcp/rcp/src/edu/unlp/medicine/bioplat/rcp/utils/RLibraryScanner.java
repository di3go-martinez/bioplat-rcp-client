package edu.unlp.medicine.bioplat.rcp.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This object parses a text file and returns the libraries declared in it. The
 * text file must have the format: library name = instructions to install the
 * library. For example:
 * inSilicoDb=source("http://www.bioconductor.org/biocLite.R")
 * 
 * @author Diego García
 * 
 */
public class RLibraryScanner {
	// Logger Object
	private static Logger logger = LoggerFactory.getLogger(RLibraryScanner.class);

	/** Template method that calls {@link #processLine(String)}. */
	public final List<RLibrary> processLineByLine(final InputStream input) throws FileNotFoundException {
		List<RLibrary> libraries = new ArrayList<RLibrary>();
		Scanner scanner = new Scanner(input);
		RLibrary library;
		try {
			// first use a Scanner to get each line
			while (scanner.hasNextLine()) {
				library = processLine(scanner.nextLine());
				if (library != null) {
					libraries.add(library);
				}
			}
		} finally {
			scanner.close();
		}
		return libraries;
	}

	protected RLibrary processLine(String aLine) {
		// use a second Scanner to parse the content of each line
		Scanner scanner = new Scanner(aLine);
		scanner.useDelimiter("=");
		if (scanner.hasNext()) {
			String name = scanner.next();
			String value = scanner.next();
			return new RLibrary(name, value);

		} else {
			logger.info("Empty or invalid line. Unable to process.");
		}
		// no need to call scanner.close(), since the source is a String
		return null;
	}

}
