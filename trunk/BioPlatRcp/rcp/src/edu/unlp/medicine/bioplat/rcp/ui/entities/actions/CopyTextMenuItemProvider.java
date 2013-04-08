package edu.unlp.medicine.bioplat.rcp.ui.entities.actions;

import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;

/**
 * @see CopyColumnTextMenuItemDescriptor
 * @author diego martínez
 * 
 */
public interface CopyTextMenuItemProvider {
	String name();

	Accesor accesor();

	List<?> elements();
}
