package edu.unlp.medicine.bioplat.core;

import javax.annotation.Nullable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class Activator extends AbstractUIPlugin {

	public Activator() {
		// TODO Auto-generated constructor stub
	}

	private static final ImageDescriptor DEFAULT = imageDescriptorFromPlugin("resources/icons/default.png");
	private static final String RESOURCES_ICONS = "resources/icons/";

	public static String id() {
		return "edu.unlp.medicine.bioplat.core";
	}

	/**
	 * 
	 * 
	 * Buscar una imagen segun imageFilePath
	 * 
	 * @param imageFilePath
	 *            puede ser el path absoluto de la imagen o un path relativo a
	 *            RESOURCES_ICONS
	 * @return el descriptor de la imagen encontrada o un descriptor por DEFAULT
	 *         en caso de no encontrarla.
	 * 
	 * @see Activator#imageDescriptorFromPlugin(String, String)
	 */
	public static ImageDescriptor imageDescriptorFromPlugin(@Nullable String imageFilePath) {

		if (imageFilePath == null)
			return DEFAULT;

		if (!imageFilePath.contains(RESOURCES_ICONS))
			imageFilePath = RESOURCES_ICONS + imageFilePath;

		ImageDescriptor id = imageDescriptorFromPlugin(id(), imageFilePath);
		if (id == null)
			id = DEFAULT;
		return id;

	}
}
