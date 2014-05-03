package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues;

import org.eclipse.swt.events.SelectionListener;

/**
 * 
 * @author Diego Martínez
 * @see MenuItemContribution
 */
// TODO analizar si vale la pena....
public interface MenuContribution {

	MenuContribution text(String label);

	MenuContribution addSelectionListener(SelectionListener listener);

	MenuContribution removeSelectionListener(SelectionListener listener);

	boolean selection();

}
