package edu.unlp.medicine.bioplat.rcp.utils;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class PlatformUtils {

	private PlatformUtils() {
	}

	public static IEclipsePreferences preferences() {
		// TODO externalizar el id del activator del plugin de genes!!
		return ConfigurationScope.INSTANCE.getNode("edu.unlp.medicine.bioplat.genes");
	}

}
