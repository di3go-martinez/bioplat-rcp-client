package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;

/**
 * Configurador para cada columna de una grilla
 * 
 * @author diego
 * 
 */
//FIXME no se puede implementar correctamente, por limitaciones propias y ajenas
public class MenuConfigurerByRightClick implements IMenuConfigurer {

	private TableViewer viewer;
	private Menu tableHeaderPopup;

	public MenuConfigurerByRightClick(TableViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * 
	 * configura la columna tvc con el columnBuilder
	 * 
	 * @param tvc
	 *            es la columna creada
	 * @param columnBuilder
	 *            es el constructor de la columna, contiene, entre otros, la
	 *            lista de items de menú a agregar
	 */
	@Override
	public void configure(final TableViewerColumn tvc, ColumnBuilder columnBuilder) {

		// modularizar y hacer configurable desde afuera
		final Table innerTable = viewer.getTable();

		final Shell shell = innerTable.getParent().getShell();
		final Display display = shell.getDisplay();

		// Le pongo que por default el click haga el orden de la columna
		TableColumn column = tvc.getColumn();
		int colNumber = innerTable.indexOf(column);
		column.addSelectionListener(columnBuilder.getSelectionSorterAdapter(viewer, column, colNumber));

		tableHeaderPopup = new Menu(shell, SWT.POP_UP);
		// TODO soporte para remover los listeners en caso de ser necesario...
		innerTable.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				boolean header = isMouseOverHeader(innerTable, display, new Point(event.x, event.y));
				boolean onColumn = isOverColumn(innerTable, display, new Point(event.x, event.y), tvc);
				// Acá se podría distinguir el menú para cada fila del menú para
				// el header
				if (header && onColumn)
					// FIXME siempre asigna el último... habría que poder
					// distinguir la columna en al que se está haciendo click...
					innerTable.setMenu(tableHeaderPopup);
				// se muestra sólo el menú del header por ahora
				event.doit = header;
			}

		});

		/*
		 * IMPORTANT: Dispose the menus (only the current menu, set with
		 * setMenu(), will be automatically disposed) (C&P)
		 */
		innerTable.addListener(SWT.Dispose, new Listener() {
			@Override
			public void handleEvent(Event event) {
				tableHeaderPopup.dispose();
				innerTable.removeListener(SWT.Dispose, this);
			}
		});

		for (MenuItemDescriptor mid : columnBuilder.headerMenuItemDescriptors()) {
			mid.createOn(tableHeaderPopup, tvc);
		}
	}

	/**
	 * 
	 * @param innerTable
	 * @param display
	 * @param mouse
	 * @return true si la posición donde se hizo click es el area de la
	 *         cabecera, false en otro caso
	 */
	private boolean isMouseOverHeader(final Table innerTable, final Display display, Point mouse) {
		Point pt = display.map(null, innerTable, mouse);
		Rectangle clientArea = innerTable.getClientArea();
		boolean header = clientArea.y <= pt.y && pt.y < (clientArea.y + innerTable.getHeaderHeight());
		return header;
	}

	/**
	 * 
	 * @param table
	 * @param display
	 * @param mouse
	 *            Mouse position point
	 * @param tvc
	 *            columna a chequear
	 * @return true si el click se realizó sobre la columna <cede>tvc</code>
	 */
	// FIXME si la grilla tiene un scrollbar horizontal deja de tener sentido
	// esta implementación
	private boolean isOverColumn(Table table, Display display, Point mouse, TableViewerColumn tvc) {
		Point pt = display.map(null, table, mouse);
		int base = 0;
		for (TableColumn tc : table.getColumns()) {
			int offset = base + tc.getWidth();
			if (pt.x > base && pt.x < offset) {
				return tvc.getColumn().equals(tc);
			}
			base += tc.getWidth();
		}
		return false;
	}
}
