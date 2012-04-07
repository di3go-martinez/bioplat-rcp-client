package edu.unlp.medicine.bioplat.rcp.application;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class Activator extends AbstractUIPlugin {

	private static final String RESOURCES_ICONS = "resources/icons/";

	public static String id() {
		return "edu.medicine.bioplat.rcp";
	}

	public Activator() {
		// TODO Auto-generated constructor stub
	}

	public static ImageDescriptor imageDescriptorFromPlugin(String imageFilePath) {
		if (!imageFilePath.contains(RESOURCES_ICONS))
			imageFilePath = RESOURCES_ICONS + imageFilePath;

		ImageDescriptor id = imageDescriptorFromPlugin(id(), imageFilePath);
		if (id == null)
			id = imageDescriptorFromPlugin("resources/icons/default.png");
		return id;

	}

}
