package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.IMenuConfigurer;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemDescriptor;

/**
 * 
 * Configura el menu en la cabecera de una grilla. El menú aparacerá al hacer
 * click sobre la correspondiente columna
 * 
 * @author Diego Martínez
 */
public class MenuConfigurerByHeaderSelection implements IMenuConfigurer {

	private TableViewer viewer;

	public MenuConfigurerByHeaderSelection(TableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void configure(final TableViewerColumn tvc, final ColumnBuilder columnBuilder) {
		// modularizar y hacer configurable desde afuera
		final Table innerTable = viewer.getTable();

		final Shell shell = innerTable.getParent().getShell();
		final Display display = shell.getDisplay();

		final Menu tableHeaderPopup = new Menu(shell, SWT.POP_UP);
		for (MenuItemDescriptor mid : columnBuilder.headerMenuItemDescriptors())
			mid.createOn(tableHeaderPopup, tvc);

		final TableColumn column = tvc.getColumn();
		column.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				innerTable.setMenu(tableHeaderPopup);
				final Menu menu = innerTable.getMenu();
				menu.setVisible(true);

				// Agrego un listener para desasociar el menu, ya que sino al
				// hacer click secundario se activa un menu que no corresponde
				menu.addMenuListener(new MenuListener() {

					@Override
					public void menuShown(MenuEvent e) {

					}

					@Override
					public void menuHidden(MenuEvent e) {
						columnBuilder.reconfigureMenu();
						menu.removeMenuListener(this);
					}
				});
			}
		});

		// column.addListener(SWT.MouseHover, new Listenerr("Mouse Hover",
		// innerTable));
		// column.addListener(SWT.MouseEnter, new Listenerr("mouse enter",
		// innerTable));
		// column.addListener(SWT.MouseMove, new Listenerr("Mouse move",
		// innerTable));
		// column.addListener(SWT.MouseDown, new Listenerr("Mouse down",
		// innerTable));
		// column.addListener(SWT.MenuDetect, new Listenerr("Menu detect",
		// innerTable));

	}

	private class Listenerr implements Listener {

		private String type;
		private Table table;

		public Listenerr(String string, Table table) {
			this.type = string;
			this.table = table;
		}

		@Override
		public void handleEvent(Event event) {
			System.out.println("event detected " + type);
			boolean r = (isMouseOverHeader(table, table.getParent().getShell().getDisplay(), new Point(event.x, event.y)));
			System.out.println("Mouse is " + ((r) ? "" : " not ") + " over the header");
			;
		}
	}

	/**
	 * 
	 * @param innerTable
	 * @param mouse
	 * @return true si la posición donde se hizo click es el area de la
	 *         cabecera, false en otro caso
	 */
	private boolean isMouseOverHeader(final Table innerTable, Display display, Point mouse) {
		Point pt = display.map(null, innerTable, mouse);
		Rectangle clientArea = innerTable.getClientArea();
		boolean header = clientArea.y <= pt.y && pt.y < (clientArea.y + innerTable.getHeaderHeight());
		return header;
	}

}
