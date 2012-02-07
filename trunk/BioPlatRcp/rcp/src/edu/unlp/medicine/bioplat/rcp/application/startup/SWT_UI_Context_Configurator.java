package edu.unlp.medicine.bioplat.rcp.application.startup;

import org.eclipse.ui.IStartup;

import edu.unlp.medicine.bioplat.rcp.ui.entities.SWTUIContext;
import edu.unlp.medicine.entity.generic.ContextFactory;

public class SWT_UI_Context_Configurator implements IStartup {

	@Override
	public void earlyStartup() {
		ContextFactory.set(new SWTUIContext());
	}

}
