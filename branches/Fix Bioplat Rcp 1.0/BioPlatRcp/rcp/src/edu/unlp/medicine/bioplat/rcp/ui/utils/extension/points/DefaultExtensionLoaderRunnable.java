package edu.unlp.medicine.bioplat.rcp.ui.utils.extension.points;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

public class DefaultExtensionLoaderRunnable implements ExtensionLoaderRunnable {

	@Override
	public void handleException(Throwable e) {
		// TODO Auto-generated method stub
		e.printStackTrace();
	}

	/**
	 * Para cada elemento de configs, ejecuta make(elemento)
	 */
	@Override
	public void run(List<IConfigurationElement> configs) throws Exception {
		for (IConfigurationElement iConfigurationElement : configs)
			runOn(iConfigurationElement);
	}

	protected void runOn(IConfigurationElement celement) throws Exception {
		System.out.println(celement);
	}
}
