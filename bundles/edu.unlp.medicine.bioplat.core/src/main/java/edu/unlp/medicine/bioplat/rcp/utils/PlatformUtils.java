package edu.unlp.medicine.bioplat.rcp.utils;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.utils.events.GeneChangeEvent;

public class PlatformUtils {

	private PlatformUtils() {
	}

	/**
	 * Cuidado con esto, después del refactor de plugins puede no andar, en caso de
	 * no funcionar invocar a preferences con el id correspondiente al
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

	public static enum eventbus {
		instance;
		private EventBus eb = new EventBus();

		public void register(Object annotatedObject) {
			eb.register(annotatedObject);
		}

		public void post(GeneChangeEvent geneChangeEvent) {
			eb.post(geneChangeEvent);
		}

		public void unregister(Object registeredObject) {
			try {
				eb.unregister(registeredObject);
			} catch (IllegalArgumentException iae) {
				logger.warn(registeredObject+ " is not registered... ignoring");
			}

		}
	}
	
	private static final Logger logger = LoggerFactory.getLogger(PlatformUIUtils.class);
}
