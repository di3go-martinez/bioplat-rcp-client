package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues;

import org.eclipse.swt.events.SelectionListener;

/**
 * 
 * @author diego
 * @see MenuItemContribution
 */
// TODO analizar si vale la pena....
public interface MenuContribution {

	MenuContribution text(String label);

	void addSelectionListener(SelectionListener listener);

	void removeSelectionListener(SelectionListener listener);

	boolean selection();

}
