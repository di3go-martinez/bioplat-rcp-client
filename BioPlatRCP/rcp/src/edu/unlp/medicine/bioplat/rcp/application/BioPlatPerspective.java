package edu.unlp.medicine.bioplat.rcp.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import edu.unlp.medicine.bioplat.rcp.ui.genes.view.GeneViewPart;

public class BioPlatPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addPlaceholder("*view*", IPageLayout.BOTTOM, 0.6f, layout.getEditorArea());
		layout.addView(GeneViewPart.id(), IPageLayout.RIGHT, 0.5f, layout.getEditorArea());
		// layout.addView(PersonaViewPart.id(), IPageLayout.RIGHT, 0.5f,
		// layout.getEditorArea());
	}

	public static String id() {
		return "bioplatrcp.perspective"; //$NON-NLS-1$
	}
}
