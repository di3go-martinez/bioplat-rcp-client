package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

/**
 * Descriptor para crear menúes que ordenan una columna y van intercalando el orden
 * 
 * @author Diego Martínez
 * @see ColumnBuilder#getSelectionSorterAdapter(TableViewer, org.eclipse.swt.widgets.TableColumn, int)
 */
public class ToggleSortColumnMenuItemDescriptor implements MenuItemDescriptor {

	private TableViewer viewer;
	private ColumnBuilder cb;

	public ToggleSortColumnMenuItemDescriptor(ColumnBuilder cb, TableViewer viewer) {
		this.cb = cb;
		this.viewer = viewer;
	}

	@Override
	public MenuContribution createOn(Menu parent, TableViewerColumn column) {
		MenuItemContribution mc = MenuItemContribution.create(parent);
		mc.text("Toggle Order");
		mc.image(PlatformUIUtils.findImage("asc.png"));
		SelectionAdapter selectionAdapter;
		Table t = viewer.getTable();
		int index = t.indexOf(column.getColumn());

		selectionAdapter = cb.getSelectionSorterAdapter(viewer, column.getColumn(), index);

		mc.addSelectionListener(selectionAdapter);
		return mc;
	}
}
