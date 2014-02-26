package edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues;

import org.eclipse.jface.viewers.TableViewerColumn;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;

//TODO extraer métodos para esta interface
public interface IMenuConfigurer {

	public void configure(final TableViewerColumn tvc, final ColumnBuilder columnBuilder);
	// IMenuConfigurer add(MenuContribution item);
}
