package edu.unlp.medicine.bioplat.rcp.application;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Diego Martínez
 * 
 */
public class Activator extends AbstractUIPlugin {
	private static Logger logger = LoggerFactory.getLogger(Activator.class);
	private static final ImageDescriptor DEFAULT = imageDescriptorFromPlugin("resources/icons/default.png");
	private static final String RESOURCES_ICONS = "resources/icons/";

	public static String id() {
		return "edu.medicine.bioplat.rcp";
	}

	public Activator() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);

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
