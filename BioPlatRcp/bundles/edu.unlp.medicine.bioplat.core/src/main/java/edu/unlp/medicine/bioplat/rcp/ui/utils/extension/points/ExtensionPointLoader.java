package edu.unlp.medicine.bioplat.rcp.ui.utils.extension.points;

import java.util.Arrays;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;

/**
 * 
 * 
 * Cargador de extension points
 * 
 * @author diego
 * 
 * @see ExtensionLoaderRunnable
 */
public class ExtensionPointLoader {

	private String extensionPointId;

	private ExtensionPointLoader(String extensionPointId) {
		this.extensionPointId = extensionPointId;
	}

	public static ExtensionPointLoader create(String extensionPointId) {
		return new ExtensionPointLoader(extensionPointId);
	}

	public void load(final ExtensionLoaderRunnable elr) {
		final IConfigurationElement[] configs = Platform.getExtensionRegistry().getConfigurationElementsFor(extensionPointId);
		ISafeRunnable runnable = new ISafeRunnable() {
			@Override
			public void handleException(Throwable exception) {
				elr.handleException(exception);
			}

			@Override
			public void run() throws Exception {
				elr.run(Arrays.asList(configs));
			}
		};
		SafeRunner.run(runnable);
	}

}
