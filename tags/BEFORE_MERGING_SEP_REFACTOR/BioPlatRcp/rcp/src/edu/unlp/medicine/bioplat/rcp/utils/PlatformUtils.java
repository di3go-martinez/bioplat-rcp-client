package edu.unlp.medicine.bioplat.rcp.utils;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import edu.unlp.medicine.bioplat.rcp.application.Activator;

public class PlatformUtils {

	private PlatformUtils() {
	}

	public static IEclipsePreferences preferences() {
		return ConfigurationScope.INSTANCE.getNode(Activator.id());
	}

}
