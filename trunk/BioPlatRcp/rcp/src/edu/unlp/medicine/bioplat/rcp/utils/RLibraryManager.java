package edu.unlp.medicine.bioplat.rcp.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.domainLogic.framework.statistics.rIntegration.jri.RRunnerUsingJRI;
import edu.unlp.medicine.r4j.environments.R4JSession;
import edu.unlp.medicine.r4j.exceptions.R4JConnectionException;
import edu.unlp.medicine.r4j.values.R4JValue;

public class RLibraryManager {
	/**
	 * Representa el número máximo de intentos para conectarse a Rserve.
	 */
	private static final int TEST_MAX_NUMBER = 6;
	/**
	 * Logger Object
	 */
	private static Logger logger = LoggerFactory.getLogger(RLibraryManager.class);

	/**
	 * This method validates if the libraries are installed. returns a
	 * collection of libraries not installed
	 */
	public List<RLibrary> getLibrariesNotInstalled(final Iterator<RLibrary> libraries) {
		List<RLibrary> librariesNotInstalled = new ArrayList<RLibrary>();
		try {
			RRunnerUsingJRI.getInstance().openSession();
			RLibrary library;
			while (libraries.hasNext()) {
				library = libraries.next();
				if (!isInstalled(library)) {
					// libreria no instalada
					logger.warn("Library not installed: " + library.getName());
					librariesNotInstalled.add(library);
				}
			}
		} catch (R4JConnectionException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			RRunnerUsingJRI.getInstance().closeSession();
		}
		return librariesNotInstalled;

	}

	/**
	 * This method returns true if the library is installed. False otherwise.
	 * 
	 * @param library
	 * @return
	 */
	private boolean isInstalled(RLibrary library) {
		R4JValue value = RRunnerUsingJRI.getInstance().parseAndEval("suppressWarnings(require('" + library.getName() + "',quietly=TRUE))");
		return value.asInteger() > 0;
	}

	/**
	 * This method validates if the server is running. Otherwise it returns
	 * false.
	 * 
	 */
	public boolean isTheRserveRunning() {
		R4JSession session = new R4JSession("test");
		int testNumber = 0;
		boolean result = false;
		do {
			try {
				session.open();
				result = true;
			} catch (R4JConnectionException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			} finally {
				session.close();
			}
		} while (!result && testNumber++ < TEST_MAX_NUMBER);
		return result;
	}

	/**
	 * This method installs the libraries. Also returns the libraries that
	 * failed to install.
	 * 
	 * @param libraries
	 * @return
	 */
	public List<RLibrary> installRLibraries(final List<RLibrary> libraries) {
		List<RLibrary> librariesNotInstalled = new ArrayList<RLibrary>();
		try {
			RRunnerUsingJRI.getInstance().openSession();
			Iterator<RLibrary> iterator = libraries.iterator();
			RLibrary library;
			while (iterator.hasNext()) {
				library = iterator.next();
				RRunnerUsingJRI.getInstance().parseAndEval(library.getInstallation());
				if (!this.isInstalled(library)) {
					librariesNotInstalled.add(library);
				}
			}

		} catch (R4JConnectionException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			RRunnerUsingJRI.getInstance().closeSession();
		}
		return librariesNotInstalled;

	}

}
