package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Menu;

public interface MenuItemDescriptor {

	MenuContribution createOn(final Menu menu, final TableViewerColumn column);
}
