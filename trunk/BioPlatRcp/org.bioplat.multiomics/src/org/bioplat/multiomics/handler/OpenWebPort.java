package org.bioplat.multiomics.handler;

import org.bioplat.multiomics.WebPort;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

public class OpenWebPort extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return PlatformUIUtils.openView(WebPort.ID);
	}
}