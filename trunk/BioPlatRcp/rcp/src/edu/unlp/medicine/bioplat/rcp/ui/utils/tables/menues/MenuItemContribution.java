package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Delega en MenuItem
 * 
 * @author diego martínez
 * 
 */
public class MenuItemContribution implements MenuContribution {
	private MenuItem item;

	// /"Constructores"
	private MenuItemContribution(MenuItem menuItem) {
		this.item = menuItem;
	}

	public static MenuItemContribution create(Menu parent) {
		return create(parent, SWT.NONE);
	}

	public static MenuItemContribution create(Menu parent, int style) {
		return new MenuItemContribution(new MenuItem(parent, style));
	}

	// ///////////////////

	@Override
	public MenuContribution text(String text) {
		item.setText(text);
		return this;
	}

	@Override
	public void addSelectionListener(SelectionListener listener) {
		item.addSelectionListener(listener);

	}

	@Override
	public void removeSelectionListener(SelectionListener listener) {
		item.removeSelectionListener(listener);
	}

	@Override
	public boolean selection() {
		return item.getSelection();
	}

	public MenuItemContribution image(Image image) {
		item.setImage(image);
		return this;
	}

}
