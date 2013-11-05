package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import org.apache.commons.lang.NotImplementedException;
import org.eclipse.jface.viewers.TableViewer;
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
