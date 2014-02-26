package edu.unlp.medicine.bioplat.rcp.utils;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import edu.unlp.medicine.bioplat.core.Activator;

public class PlatformUtils {

	private PlatformUtils() {
	}

	/**
	 * Cuidado con esto, después del refactor de plugins puede no andar, en caso
	 * de no funcionar invocar a preferences con el id correspondiente al
	 * Activator.id() que corresponda
	 * 
	 * @return
	 */
	@Deprecated
	public static IEclipsePreferences preferences() {
		return ConfigurationScope.INSTANCE.getNode(Activator.id());
	}

	public static IEclipsePreferences preferences(String pluginActivatorId) {
		return ConfigurationScope.INSTANCE.getNode(pluginActivatorId);
	}

}
