package edu.unlp.medicine.bioplat.rcp.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class BioPlatPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addPlaceholder("*view*", IPageLayout.BOTTOM, 0.6f, layout.getEditorArea());

		// TODO habilitar cuando funcione bien la vista de genes
		// layout.addView(GeneViewPart.id(), IPageLayout.RIGHT, 0.5f,
		// layout.getEditorArea());

		// pruebas... sacar de acá...
		// layout.addView(PersonaViewPart.id(), IPageLayout.RIGHT, 0.5f,
		// layout.getEditorArea());
	}

	public static String id() {
		return "bioplatrcp.perspective"; //$NON-NLS-1$
	}
}
