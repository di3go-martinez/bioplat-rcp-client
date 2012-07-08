package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import org.eclipse.jface.viewers.TableViewer;
import org.jboss.util.NotImplementedException;

public class DefaultColumnManager implements ColumnManager {

	private TableViewer tableViewer;

	public DefaultColumnManager(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	@Override
	public void remove(String columnId) {
		throw new NotImplementedException();
	}

	public static ColumnManager createOn(TableViewer viewer) {
		return new DefaultColumnManager(viewer);
	}

}
