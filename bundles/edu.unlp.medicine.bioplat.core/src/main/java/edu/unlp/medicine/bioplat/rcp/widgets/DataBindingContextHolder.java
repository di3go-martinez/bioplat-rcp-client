package edu.unlp.medicine.bioplat.rcp.widgets;

import org.eclipse.core.databinding.DataBindingContext;

/**
 * Mantiene una instancia compartida de un data binding context
 * 
 * @author Diego Martínez
 */
public class DataBindingContextHolder {
	private static DataBindingContext dbc = new DataBindingContext();

	static DataBindingContext dataBindingGlobalContext() {
		return dbc;
	}
	
	private DataBindingContextHolder() {
    }
}
