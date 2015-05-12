package edu.unlp.medicine.bioplat.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//FIXME mover de paquete
//Ojo con la carga de recursos (como imágenes)
public class Activator extends AbstractUIPlugin {

	private static Logger logger = LoggerFactory.getLogger(Activator.class);

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
	public static ImageDescriptor imageDescriptorFromPlugin(String imageFilePath) {

		if (imageFilePath == null)
			return DEFAULT;

		if (!imageFilePath.contains(RESOURCES_ICONS))
			imageFilePath = RESOURCES_ICONS + imageFilePath;

		logger.debug("retrieving the image " + imageFilePath + "@" + id());
		ImageDescriptor id = imageDescriptorFromPlugin(id(), imageFilePath);
		if (id == null)
			id = DEFAULT;
		return id;

	}
}
