package edu.unlp.medicine.bioplat.rcp.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class BioPlatPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addView("org.eclipse.ui.views.ProgressView", IPageLayout.BOTTOM, 0.2f, layout.getEditorArea());
	}

	public static String id() {
		return "bioplatrcp.perspective"; //$NON-NLS-1$
	}
}
