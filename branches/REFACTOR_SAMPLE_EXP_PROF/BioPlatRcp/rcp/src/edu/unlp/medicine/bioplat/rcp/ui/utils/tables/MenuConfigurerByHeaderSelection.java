package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.IMenuConfigurer;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemDescriptor;

/**
 * 
 * Configura el menu en la cabecera de una grilla. El menú aparacerá al hacer click sobre la correspondiente columna
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
		// final Display display = shell.getDisplay();

		final Menu tableHeaderPopup = new Menu(shell, SWT.POP_UP);
		for (MenuItemDescriptor mid : columnBuilder.headerMenuItemDescriptors())
			mid.createOn(tableHeaderPopup, tvc);

		tvc.getColumn().addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				innerTable.setMenu(tableHeaderPopup);
				innerTable.getMenu().setVisible(true);
			}

		});
	}

}
