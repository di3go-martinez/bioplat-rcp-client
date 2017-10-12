package org.bioplat.multiomics.handler;

import org.bioplat.multiomics.MultiomicsWebPort;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

//TODO no está en uso, revisar plugin.xml, ubicación en menu/toolbar
public class OpenWebPortHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return PlatformUIUtils.openView(MultiomicsWebPort.ID);
	}
}