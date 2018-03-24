package edu.unlp.medicine.bioplat.rcp.ui.utils.extension.points;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

public interface ExtensionLoaderRunnable {
	void handleException(Throwable e);

	void run(List<IConfigurationElement> configs) throws Exception;
}